package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.*;
import com.FacilitiesManager.Entity.Enums.AccessRole;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Entity.Model.CabinAvaliableModel;
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
                    bookings.setStatus(BookingStatus.approved);
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
                        bookingModel.setStatus(BookingStatus.approved);
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


    public  ApiResponseModel viewBookingByCabinIdDate(CabinAvaliableModel cabinAvaliableModel)
    {
        System.out.println("Start Date:"+cabinAvaliableModel.getValidFrom());
        System.out.println("End Date:"+cabinAvaliableModel.getValidTill());
        System.out.println("Date:"+cabinAvaliableModel.getStartDate());
        System.out.println("Cabin Id"+cabinAvaliableModel.getCabinId());


        List<BookingModel> bookings=bookingModelRepository.findBookingsByCabinIdBetweenTimes(cabinAvaliableModel.getCabinId(),
                cabinAvaliableModel.getValidFrom(),
                cabinAvaliableModel.getValidTill(),
                cabinAvaliableModel.getStartDate());
        System.out.println("Cain size:"+bookings.size());
        if(bookings.isEmpty())
        {
            return new ApiResponseModel(StatusResponse.not_found, null, "User Booking not found");
        }
        else {

            for (BookingModel model:bookings)
            {
                System.out.println(model);
            }
            return new ApiResponseModel(StatusResponse.success, bookings, "User Booking");
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