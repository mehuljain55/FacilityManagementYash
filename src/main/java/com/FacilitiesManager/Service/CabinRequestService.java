package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.*;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.BookingValadity;
import com.FacilitiesManager.Entity.Enums.CabinAvaiability;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Entity.Model.CabinAvaliableModel;
import com.FacilitiesManager.Repository.BookingModelRepository;
import com.FacilitiesManager.Repository.BookingRepository;
import com.FacilitiesManager.Repository.CabinRepository;
import com.FacilitiesManager.Repository.CabinRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CabinRequestService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private CabinRequestRepository cabinRequestRepository;

    @Autowired
    private  BookingService bookingService;

    @Autowired
    private BookingModelRepository bookingModelRepository;




    public ApiResponseModel createCabinBookingRequest(CabinRequest cabinRequest, User user)
    {
        boolean isCabinAvailable;
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
            cabinRequest.setStatus(BookingStatus.hold);
            cabinRequest.setUserId(user.getEmailId());
            cabinRequestRepository.save(cabinRequest);
            return new ApiResponseModel<>(StatusResponse.success,null,"Cabin Request");
        }else {
            return new ApiResponseModel<>(StatusResponse.not_available,null,"Cabin not available");
        }
    }

    public ApiResponseModel<List<Cabin>> getAvailableCabin(CabinAvaliableModel cabinAvaliableModel)
    {
        List<Cabin> cabins=cabinRepository.findCabinByOfficeId(cabinAvaliableModel.getOfficeId());
        List<BookingModel> bookings;

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

        }else {
            bookings=bookingModelRepository.findBookingsMultipleDaysBetweenDates(cabinAvaliableModel.getStartDate(),
                    cabinAvaliableModel.getEndDate(),
                    cabinAvaliableModel.getOfficeId());
        }
        if(bookings!=null)
        {
            for(Cabin cabin:cabins)
            {
                for(BookingModel booking:bookings)
                {
                    if(cabin.getCabinId()==booking.getCabinId())
                    {
                        cabins.remove(cabin);
                    }
                }
            }
            return new ApiResponseModel<>(StatusResponse.success,cabins,"Available Cabin");
        }
        return new ApiResponseModel<>(StatusResponse.success,cabins,"Available Cabin");
    }

    public ApiResponseModel getAllCabinRequest(User user)
    {
        List<CabinRequest> cabinRequests=cabinRequestRepository.findCabinRequestByOfficeId(BookingStatus.hold, user.getOfficeId());
        List<CabinRequest> cabinRequestList=new ArrayList<>();
        if(cabinRequests!=null)
        {
            for(CabinRequest cabinRequest:cabinRequests)
            {
                boolean isCabinAvailable;
                CabinAvaiability cabinAvaiability=CabinAvaiability.Not_Available;
                if(cabinRequest.getBookingValadity().equals(BookingValadity.single_day))
                {
                    isCabinAvailable=bookingService.checkCabinAvabalitySingleDay(cabinRequest);

                }else{
                    isCabinAvailable=bookingService.checkCabinAvailabilityMultipleDay(cabinRequest);
                }
                if(isCabinAvailable)
                {
                    cabinAvaiability=CabinAvaiability.Avaliable;
                }
              cabinRequest.setCabinAvaiability(cabinAvaiability);
                cabinRequestList.add(cabinRequest);
            }
            return new ApiResponseModel<>(StatusResponse.success,cabinRequestList,"Cabin Request List");
        }
        else {
         return new ApiResponseModel<>(StatusResponse.not_found, null, "No request on hold");
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

}
