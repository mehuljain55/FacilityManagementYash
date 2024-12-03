package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.*;
import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Repository.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private CabinRequestRepository cabinRequestRepository;

    @Autowired
    private CabinRequestModelRepository cabinRequestModelRepository;

    @Autowired
    private BookingModelRepository bookingModelRepository;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private  MailingService mailingService;



    public ApiResponseModel viewBooking(User userRequest)
    {
        Optional<User> opt=userRepo.findById(userRequest.getEmailId());
        if(opt.isPresent())
        {
            User user=opt.get();
            List<Bookings> bookings=bookingRepository.findBookingsByOfficeId(user.getOfficeId());
            return new ApiResponseModel<>(StatusResponse.success,bookings,"Booking List");
        }else{
            return new ApiResponseModel<>(StatusResponse.unauthorized,null,"Unauthorized request");

        }


    }

    public ApiResponseModel createBooking(CabinRequest cabinRequestApproval)
    {
        Optional<CabinRequest> opt=cabinRequestRepository.findById(cabinRequestApproval.getRequestId());
        CabinRequest cabinRequest=opt.get();
        cabinRequest.setCabinId(cabinRequestApproval.getCabinId());

        try{
            boolean avabality_status;
            if(cabinRequest.getBookingValadity().equals(BookingValadity.single_day))
            {
                avabality_status=checkCabinAvabalitySingleDay(cabinRequest);
                cabinRequest.setEndDate(cabinRequest.getStartDate());
            }
            else{
                avabality_status=checkCabinAvailabilityMultipleDay(cabinRequest);
            }

            if(avabality_status)
            {
                Bookings bookings=new Bookings();
                Optional<Cabin> opt1=cabinRepository.findById(cabinRequestApproval.getCabinId());
                if(opt1.isPresent())
                {
                    Cabin cabin=opt1.get();
                    bookings.setCabinId(cabinRequestApproval.getCabinId());
                    bookings.setPurpose(cabinRequest.getPurpose());
                    bookings.setUserId(cabinRequest.getUserId());
                    bookings.setOfficeId(cabin.getOfficeId());
                    bookings.setStartDate(cabinRequest.getStartDate());
                    bookings.setEndDate(cabinRequest.getEndDate());
                    bookings.setValidFrom(cabinRequest.getValidFrom());
                    bookings.setValidTill(cabinRequest.getValidTill());
                    cabinRequest.setCabinId(cabinRequestApproval.getCabinId());
                    cabinRequest.setCabinName(cabin.getCabinName());
                    cabinRequest.setStatus(BookingStatus.approved);
                    Bookings bookingRequest= bookingRepository.save(bookings);
                    List<Date> dates=getDatesBetween(cabinRequest.getStartDate(),cabinRequest.getEndDate());

                    for(Date date:dates)
                    {
                        BookingModel bookingModel=new BookingModel();
                        bookingModel.setBookingId(bookingRequest.getBookingId());
                        bookingModel.setDate(date);
                        bookingModel.setCabinId(bookingRequest.getCabinId());
                        bookingModel.setPurpose(cabinRequest.getPurpose());
                        bookingModel.setUserId(cabinRequest.getUserId());
                        bookingModel.setOfficeId(cabin.getOfficeId());
                        bookingModel.setValidFrom(cabinRequest.getValidFrom());
                        bookingModel.setValidTill(cabinRequest.getValidTill());
                        bookingModelRepository.save(bookingModel);
                    }

                    List<CabinRequestModel> cabinRequestModels=cabinRequestModelRepository.findCabinRequestByCabinRequestId(cabinRequest.getRequestId());
                    for(CabinRequestModel cabinRequestModel:cabinRequestModels)
                    {
                        cabinRequestModel.setStatus(BookingStatus.approved);
                        cabinRequestModelRepository.save(cabinRequestModel);
                    }

                    CabinRequest cabinRequestUser= cabinRequestRepository.save(cabinRequest);
                    String content=mailingService.createApprovalMail(cabinRequestUser);
                    List<String> managers=userRepo.findEmailsByRoleAndOfficeId(AccessRole.manager,cabinRequestUser.getOfficeId());
                    mailingService.sendMail(managers,"Cabin request approval notification",content,cabinRequestUser.getUserId());
                    return  new ApiResponseModel<>(StatusResponse.success,null,"Booking successfully created");
                }else {
                    return  new ApiResponseModel<>(StatusResponse.not_found,null,"Cabin not found");
                }

            }else {
                return  new ApiResponseModel<>(StatusResponse.not_available,null,"Cabin already booked");
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            return  new ApiResponseModel<>(StatusResponse.failed,null,"Please contact administrator");
        }
    }

    public ApiResponseModel createBookingVip(CabinRequest cabinRequestApproval,String userId)
    {
        List<BookingModel> bookingModelList;
      Bookings booking;
      Optional<User> optionalUser=userRepo.findById(userId);
      User user=optionalUser.get();
      cabinRequestApproval.setUserId(userId);
      Optional<Cabin> cabinOptional=cabinRepository.findById(cabinRequestApproval.getCabinId());
      Cabin cabin=cabinOptional.get();
        try{
            if(cabinRequestApproval.getBookingValadity().equals(BookingValadity.single_day))
            {bookingModelList = bookingModelRepository.findBookingsByCabinIdSingleDayBetweenTimes(
                    cabinRequestApproval.getCabinId(),
                    cabinRequestApproval.getValidFrom(),
                    cabinRequestApproval.getValidTill(),
                    cabinRequestApproval.getStartDate(),
                    BookingStatus.approved
                );
            }
            else{
                cabinRequestApproval.setValidFrom(LocalTime.of(0,0));
                cabinRequestApproval.setValidTill(LocalTime.of(23,0));
                bookingModelList = bookingModelRepository.findBookingByCabinIdDate(cabinRequestApproval.getStartDate(),
                      cabinRequestApproval.getEndDate(),
                      cabinRequestApproval.getCabinId(),
                      BookingStatus.approved);
            }
            // Cancel the previous booking if found

            if(bookingModelList!=null && bookingModelList.size()>0) {
                Optional<Bookings> opt = bookingRepository.findById(bookingModelList.get(0).getBookingId());
                booking = opt.get();
                booking.setStatus(BookingStatus.cancelled);
                List<BookingModel> bookingModel = bookingModelRepository.findByBookingId(booking.getBookingId());
                for (BookingModel bookingList : bookingModel) {
                    bookingList.setStatus(BookingStatus.cancelled);
                    bookingModelRepository.save(bookingList);
                }
                Optional<CabinRequest> cabinRequestOptional=cabinRequestRepository.findById(booking.getCabinRequestId());
                CabinRequest cabinRequest=cabinRequestOptional.get();
                cabinRequest.setStatus(BookingStatus.cancelled);
                List<CabinRequestModel> cabinRequestModels=cabinRequestModelRepository.findCabinRequestByCabinRequestId(cabinRequest.getRequestId());
                for(CabinRequestModel cabinRequestModel:cabinRequestModels)
                {
                    cabinRequestModel.setStatus(BookingStatus.cancelled);
                    cabinRequestModelRepository.save(cabinRequestModel);
                }
                String reason="Your cabin request with request id:"+ cabinRequest.getRequestId()+" has been cancelled due to administrative reason please contact facility manager";
                String content=mailingService.createCustomTemplate(reason);
                List<String> managers=userRepo.findEmailsByRoleAndOfficeId(AccessRole.manager,cabinRequest.getOfficeId());
                mailingService.sendIndividual(cabinRequest.getUserId(),"Cabin Request cancellation notification",content,managers);
                cabinRequestRepository.save(cabinRequest);
                bookingRepository.save(booking);
            }
            // Create new booking for user
            CabinRequest cabinRequestSave=new CabinRequest();
            cabinRequestSave.setCabinId(cabin.getCabinId());
            cabinRequestSave.setCabinName(cabin.getCabinName());
            cabinRequestSave.setRequestDate(new Date());
            cabinRequestSave.setPurpose(cabinRequestApproval.getPurpose());
            cabinRequestSave.setStartDate(cabinRequestApproval.getStartDate());
            cabinRequestSave.setEndDate(cabinRequestApproval.getEndDate());
            cabinRequestSave.setValidFrom(cabinRequestApproval.getValidFrom());
            cabinRequestSave.setValidTill(cabinRequestApproval.getValidTill());
            cabinRequestSave.setStatus(BookingStatus.approved);
            cabinRequestSave.setUserId(cabinRequestApproval.getUserId());
            cabinRequestSave.setOfficeId(cabinRequestApproval.getOfficeId());
            cabinRequestSave.setBookingValadity(cabinRequestApproval.getBookingValadity());
            CabinRequest cabinRequestUser=cabinRequestRepository.save(cabinRequestSave);

            List<Date> dates=getDatesBetween(cabinRequestApproval.getStartDate(),cabinRequestApproval.getEndDate());
            for(Date date: dates)
            {

                CabinRequestModel cabinRequestModel=new CabinRequestModel();
                cabinRequestModel.setCabinRequestId(cabinRequestUser.getRequestId());
                cabinRequestModel.setCabinId(cabinRequestUser.getCabinId());
                cabinRequestModel.setCabinName(cabin.getCabinName());
                cabinRequestModel.setDate(date);
                cabinRequestModel.setValidFrom(cabinRequestUser.getValidFrom());
                cabinRequestModel.setValidTill(cabinRequestUser.getValidTill());
                cabinRequestModel.setPurpose(cabinRequestUser.getPurpose());
                cabinRequestModel.setUserId(cabinRequestUser.getUserId());
                cabinRequestModel.setOfficeId(cabinRequestUser.getOfficeId());
                cabinRequestModel.setStatus(cabinRequestUser.getStatus());
                cabinRequestModelRepository.save(cabinRequestModel);
            }
            Bookings bookingRequest=new Bookings();
            bookingRequest.setCabinId(cabinRequestApproval.getCabinId());
            bookingRequest.setPurpose(cabinRequestUser.getPurpose());
            bookingRequest.setUserId(cabinRequestUser.getUserId());
            bookingRequest.setOfficeId(cabin.getOfficeId());
            bookingRequest.setStartDate(cabinRequestUser.getStartDate());
            bookingRequest.setEndDate(cabinRequestUser.getEndDate());
            bookingRequest.setValidFrom(cabinRequestUser.getValidFrom());
            bookingRequest.setValidTill(cabinRequestUser.getValidTill());
            bookingRequest.setStatus(BookingStatus.approved);
            bookingRepository.save(bookingRequest);

            for(Date date:dates)
            {
                BookingModel bookingModel=new BookingModel();
                bookingModel.setBookingId(bookingRequest.getBookingId());
                bookingModel.setDate(date);
                bookingModel.setCabinId(bookingRequest.getCabinId());
                bookingModel.setPurpose(bookingRequest.getPurpose());
                bookingModel.setUserId(bookingRequest.getUserId());
                bookingModel.setOfficeId(cabin.getOfficeId());
                bookingModel.setValidFrom(bookingRequest.getValidFrom());
                bookingModel.setValidTill(bookingRequest.getValidTill());
                bookingModel.setStatus(bookingRequest.getStatus());
                bookingModelRepository.save(bookingModel);
            }

            String content=mailingService.createApprovalMail(cabinRequestUser);
            List<String> managers=userRepo.findEmailsByRoleAndOfficeId(AccessRole.manager,bookingRequest.getOfficeId());
            mailingService.sendIndividual(bookingRequest.getUserId(),"Booking request approved",content,managers);
            return  new ApiResponseModel<>(StatusResponse.success,null,"Booking created");

   }catch (Exception e)
        {
            e.printStackTrace();
            return  new ApiResponseModel<>(StatusResponse.failed,null,"Please contact administrator");
        }
    }

    public ApiResponseModel cancelBookingRequest(CabinRequest cabinRequestApproval) throws MessagingException {
        Optional<CabinRequest> opt=cabinRequestRepository.findById(cabinRequestApproval.getRequestId());
        CabinRequest cabinRequest=opt.get();
        List<CabinRequestModel> cabinRequestModels=cabinRequestModelRepository.findCabinRequestByCabinRequestId(cabinRequest.getRequestId());

        if(opt.isPresent())
        {
            cabinRequest.setStatus(BookingStatus.rejected);
            for(CabinRequestModel cabinRequestModel:cabinRequestModels)
            {
                cabinRequestModel.setStatus(BookingStatus.rejected);
                cabinRequestModelRepository.save(cabinRequestModel);
            }
            CabinRequest cabinRequestUser=    cabinRequestRepository.save(cabinRequest);
            String content=mailingService.createRejectionMail(cabinRequestUser);
            List<String> managers=userRepo.findEmailsByRoleAndOfficeId(AccessRole.manager,cabinRequestUser.getOfficeId());
            mailingService.sendMail(managers,"Cabin request rejection notification",content,cabinRequestUser.getUserId());


            return  new ApiResponseModel<>(StatusResponse.success,null,"Booking request cancelled");

        }
        else {
            return  new ApiResponseModel<>(StatusResponse.not_found,null,"Booking request not found");

        }

    }


    public boolean checkCabinAvabalitySingleDay(CabinRequest cabinRequest) {
        List<BookingModel> bookings = bookingModelRepository.findBookingsByCabinIdSingleDayBetweenTimes(
                cabinRequest.getCabinId(),
                cabinRequest.getValidFrom(),
                cabinRequest.getValidTill(),
                cabinRequest.getStartDate(),
                BookingStatus.approved
        );
        return bookings == null || bookings.isEmpty();
    }

    public boolean checkCabinAvailabilityMultipleDay(CabinRequest cabinRequest) {

        List<BookingModel> bookings = bookingModelRepository.findBookingByCabinIdDate(cabinRequest.getStartDate(),
                cabinRequest.getEndDate(),
                cabinRequest.getCabinId(),
                BookingStatus.approved);
        System.out.println("Booking multiple day");
        for(BookingModel bookingModel:bookings)
        {
            System.out.println(bookingModel);
        }

        return bookings == null || bookings.isEmpty();
    }



    public boolean checkCabinRequestSingleDay(CabinRequest cabinRequest) {
        System.out.println("Cabin Request"+cabinRequest);
        List<CabinRequestModel> bookings = cabinRequestModelRepository.findCabinBookingRequestByCabinSingleDay(
                cabinRequest.getCabinId(),
                cabinRequest.getValidFrom(),
                cabinRequest.getValidTill(),
                cabinRequest.getStartDate(),
                BookingStatus.hold
        );
        return bookings == null ||  bookings.size()<=1;
    }

    public boolean checkCabinRequestMultipleDay(CabinRequest cabinRequest) {
        List<CabinRequestModel> bookings = cabinRequestModelRepository.findCabinBookingRequestByCabinMultipleDay(cabinRequest.getStartDate(),
                cabinRequest.getEndDate(),
                cabinRequest.getCabinId(),
                BookingStatus.hold);

        return bookings == null || bookings.size()<=1;
    }






    public static List<Date> getDatesBetween(Date startDate, Date endDate)  {
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }


}