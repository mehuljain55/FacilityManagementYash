package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.*;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.CabinAvaiability;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Entity.Model.CabinAvaliableModel;
import com.FacilitiesManager.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.util.*;

@Service
public class CabinRequestService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private CabinRequestRepository cabinRequestRepository;

    @Autowired
    private CabinRequestModelRepository cabinRequestModelRepository;

    @Autowired
    private  BookingService bookingService;

    @Autowired
    private BookingModelRepository bookingModelRepository;

    @Autowired
    private UserRepository userRepo;




    public ApiResponseModel createCabinBookingRequest(CabinRequest cabinRequest, User user)
    {
        boolean isCabinAvailable;

        Optional<Cabin> opt=cabinRepository.findById(cabinRequest.getCabinId());
        if(opt.isPresent())
        {
            Cabin cabin=opt.get();
            if(cabinRequest.getBookingValadity().equals(BookingValadity.single_day))
        {
            isCabinAvailable=bookingService.checkCabinAvabalitySingleDay(cabinRequest);
            cabinRequest.setEndDate(cabinRequest.getStartDate());

        }else{
            isCabinAvailable=bookingService.checkCabinAvailabilityMultipleDay(cabinRequest);
            cabinRequest.setValidFrom(LocalTime.of(0,0));
            cabinRequest.setValidTill(LocalTime.of(23,0));
        }

        if(isCabinAvailable)
        {
            List<Date> dates=getDatesBetween(cabinRequest.getStartDate(),cabinRequest.getEndDate());

            cabinRequest.setStatus(BookingStatus.hold);
            cabinRequest.setUserId(user.getEmailId());
            cabinRequest.setCabinName(cabin.getCabinName());
           CabinRequest cabinRequestUser= cabinRequestRepository.save(cabinRequest);
           for(Date date:dates)
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
            return new ApiResponseModel<>(StatusResponse.success,null,"Cabin Request");
        }else {
            return new ApiResponseModel<>(StatusResponse.not_available,null,"Cabin not available");
        }
        }else {
            return new ApiResponseModel<>(StatusResponse.not_found,null,"Cabin not found");
        }
    }

    public ApiResponseModel addCabin(List<Cabin> cabins,String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent())
        {
            User user=opt.get();
            for(Cabin cabin:cabins)
            {
                cabin.setOfficeId(user.getOfficeId());
                cabinRepository.save(cabin);
            }
            return new ApiResponseModel(StatusResponse.success,null,"Cabin added");
        }else{
            return new ApiResponseModel(StatusResponse.unauthorized,null,"Unauthorized Request");
        }
    }

    public ApiResponseModel<List<Cabin>> getAvailableCabin(CabinAvaliableModel cabinAvaliableModel)
    {
        List<Cabin> cabins=cabinRepository.findCabinByOfficeId(cabinAvaliableModel.getOfficeId());
        List<Cabin> cabinList=new ArrayList<>();

        List<BookingModel> bookings;
        List<CabinRequestModel> cabinRequests;

        if(cabins==null)
        {
            return new ApiResponseModel<>(StatusResponse.not_found,null,"No Cabin found");
        }

        if(cabinAvaliableModel.getBookingValadity().equals(BookingValadity.single_day))
        {
            bookings=bookingModelRepository.findBookingsByOfficeIdBetweenTimes(cabinAvaliableModel.getOfficeId(),
                    cabinAvaliableModel.getValidFrom(),
                    cabinAvaliableModel.getValidTill(),
                    cabinAvaliableModel.getStartDate());
            cabinRequests=cabinRequestModelRepository.findCabinBookingRequestSingleDay(cabinAvaliableModel.getOfficeId(),
                    cabinAvaliableModel.getValidFrom(),
                    cabinAvaliableModel.getValidTill(),
                    cabinAvaliableModel.getStartDate(),
                    BookingStatus.hold);

        }else {
            bookings=bookingModelRepository.findBookingsMultipleDaysBetweenDates(cabinAvaliableModel.getStartDate(),
                    cabinAvaliableModel.getEndDate(),
                    cabinAvaliableModel.getOfficeId());
            cabinRequests=cabinRequestModelRepository.findCabinBookingRequestMultipleDay(cabinAvaliableModel.getStartDate(),
                    cabinAvaliableModel.getEndDate(),
                    cabinAvaliableModel.getOfficeId(),
                    BookingStatus.hold);
        }
        System.out.println("Cabin Request:"+cabinRequests.size());
        System.out.println("Booking Request:"+bookings.size());
        if(bookings!=null)
        {
            for(Cabin cabin:cabins)
            {
                for(CabinRequestModel cabinRequest:cabinRequests)
                {
                    if(cabin.getCabinId()==cabinRequest.getCabinId())
                    {
                        cabin.setStatus(CabinAvaiability.Requested);
                    }
                }

                for(BookingModel booking:bookings)
                {
                    if(cabin.getCabinId()==booking.getCabinId())
                    {
                        cabin.setStatus(CabinAvaiability.Booked);
                    }
                }

                if(cabin.getStatus()==null)
                {
                    cabin.setStatus(CabinAvaiability.Avaliable);
                }

                cabinList.add(cabin);


            }
            return new ApiResponseModel<>(StatusResponse.success,cabinList,"Available Cabin");
        }
        return new ApiResponseModel<>(StatusResponse.success,cabinList,"Available Cabin");
    }


    public  ApiResponseModel findAllCabin(String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent())
        {
            User user=opt.get();
            List<Cabin> cabins=cabinRepository.findCabinByOfficeId(user.getOfficeId());
            return new ApiResponseModel<>(StatusResponse.success,cabins,"Cabins List");
        }else{
            return new ApiResponseModel<>(StatusResponse.unauthorized,null,"Unauthorized request");
        }
    }

    public ApiResponseModel updateCabinList(List<Cabin> cabins)
    {
        try {
            for (Cabin cabinRequest : cabins) {
                Optional<Cabin> opt = cabinRepository.findById(cabinRequest.getCabinId());
                Cabin cabin = opt.get();
                cabin.setCabinName(cabin.getCabinName());
                cabin.setCapacity(cabinRequest.getCapacity());
                cabinRepository.save(cabin);
            }
            return new ApiResponseModel<>(StatusResponse.success, null, "Cabin updated");
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ApiResponseModel<>(StatusResponse.failed, null, "Unable to update cabin");
        }
    }


    public ApiResponseModel getAllCabinHoldRequest(User user)
    {
        List<CabinRequest> cabinRequests=cabinRequestRepository.findCabinRequestByOfficeId(BookingStatus.hold, user.getOfficeId());
        List<CabinRequest> cabinRequestList=new ArrayList<>();
        if(cabinRequests!=null)
        {
            for(CabinRequest cabinRequest:cabinRequests)
            {
                boolean isCabinAvailable;
                boolean avability;
                CabinAvaiability cabinAvaiability=CabinAvaiability.Booked;
                if(cabinRequest.getBookingValadity().equals(BookingValadity.single_day))
                {
                    isCabinAvailable=bookingService.checkCabinAvabalitySingleDay(cabinRequest);
                    avability=bookingService.checkCabinRequestSingleDay(cabinRequest);


                }else{
                    isCabinAvailable=bookingService.checkCabinAvailabilityMultipleDay(cabinRequest);
                    avability=bookingService.checkCabinRequestMultipleDay(cabinRequest);
                }


                if(isCabinAvailable)
                {
                    cabinAvaiability=CabinAvaiability.Avaliable;
                }

                if(!avability)
                {
                    cabinAvaiability=CabinAvaiability.Requested;
                }


              cabinRequest.setCabinAvaiability(cabinAvaiability);
                cabinRequestList.add(cabinRequest);
            }
            System.out.println("Cabin Request List Size:"+cabinRequestList.size());
            return new ApiResponseModel<>(StatusResponse.success,cabinRequestList,"Cabin Request List");
        }
        else {
         return new ApiResponseModel<>(StatusResponse.not_found, null, "No request on hold");
        }
    }

    public ApiResponseModel getAllCabinRequest(User user)
    {
        List<CabinRequest> cabinRequests=cabinRequestRepository.findCabinRequestByOffice(user.getOfficeId());
        if(cabinRequests!=null)
        {
            return new ApiResponseModel<>(StatusResponse.success,cabinRequests,"Cabin Request List");
        }
        else {
            return new ApiResponseModel<>(StatusResponse.not_found, null, "No Cabin request");
        }
    }

  public ApiResponseModel getCabinRequestByUser(User user)
  {
         List<CabinRequest> cabinRequests=cabinRequestRepository.findCabinRequestByUserId(user.getEmailId());
         if(cabinRequests!=null)
         {
             return new ApiResponseModel<>(StatusResponse.success,cabinRequests,"Cabin request found");
         }
         else {
             return new ApiResponseModel<>(StatusResponse.not_found,null,"No requests");
         }
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
