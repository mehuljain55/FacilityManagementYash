package com.FacilitiesManager.Service;


import com.FacilitiesManager.Entity.*;
import com.FacilitiesManager.Entity.Enums.BookingStatus;
import com.FacilitiesManager.Entity.Enums.StatusResponse;
import com.FacilitiesManager.Entity.Enums.UserApprovalStatus;
import com.FacilitiesManager.Entity.Model.*;
import com.FacilitiesManager.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CabinRequestRepository cabinRequestRepository;

    @Autowired
    private BookingModelRepository bookingModelRepository;

    @Autowired
    private CabinRepository cabinRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private OfficeRepository officeRepository;

    public ApiResponseModel getUserApprovalList(String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent()) {
            User user=opt.get();
            List<User> users = userRepo.findUserRequest(user.getOfficeId(), UserApprovalStatus.PENDING);
            if (users != null && users.size()>0) {
                return new ApiResponseModel<>(StatusResponse.success, users, "User Approval List");
            }else {
                return new ApiResponseModel<>(StatusResponse.not_found,null,"No approval pending");
            }
        }else {
            return new ApiResponseModel<>(StatusResponse.unauthorized,null,"Unauthorized user");
        }
    }

    public ApiResponseModel approveUser(String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent()) {
            User user=opt.get();
            user.setStatus(UserApprovalStatus.ACTIVE);
            userRepo.save(user);
            return new ApiResponseModel<>(StatusResponse.success,null,"User approved");
        }else {
            return new ApiResponseModel<>(StatusResponse.unauthorized,null,"Unauthorized user");
        }
    }

    public ApiResponseModel userApprovalCancel(String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent()) {
            User user=opt.get();
            user.setStatus(UserApprovalStatus.BLOCKED);
            userRepo.save(user);
            return new ApiResponseModel<>(StatusResponse.success,null,"User blocked");
        }else {
            return new ApiResponseModel<>(StatusResponse.unauthorized,null,"Unauthorized user");
        }
    }

    public ApiResponseModel getRequestCountModel(String officeId)
    {
       int cabinRequestHold=(cabinRequestRepository.findCabinRequestByOfficeId(BookingStatus.hold,officeId)).size();
       int cabinRequestApproved=(cabinRequestRepository.findCabinRequestByOfficeId(BookingStatus.approved,officeId)).size();
       int cabinRequestRejected=(cabinRequestRepository.findCabinRequestByOfficeId(BookingStatus.rejected,officeId)).size();
       int userRequestPending=(userRepo.findUserRequest(officeId,UserApprovalStatus.PENDING)).size();
       int userRequestApproved=(userRepo.findUserRequest(officeId,UserApprovalStatus.ACTIVE)).size();
       int todaysBooking=(bookingModelRepository.findBookingsMultipleDaysBetweenDates(new Date(),new Date(),officeId)).size();
        DashboardModel dashboardModel=new DashboardModel();
        dashboardModel.setCabinRequestHold(cabinRequestHold);
        dashboardModel.setUserRequestApproved(cabinRequestApproved);
        dashboardModel.setCabinRequestRejected(cabinRequestRejected);
        dashboardModel.setUserRequestPending(userRequestPending);
        dashboardModel.setUserRequestApproved(userRequestApproved);
        dashboardModel.setTodaysCabinBooking(todaysBooking);
        return new ApiResponseModel<>(StatusResponse.success,dashboardModel,"Manager dashboard");

    }

    public  ApiResponseModel getAllCabinView( ApiRequestBookingViewModel apiRequestModel)
    {
           List<Cabin> cabins=cabinRepository.findCabinByOfficeId(apiRequestModel.getOfficeId());
            List<CabinModelList> cabinModelList=new ArrayList<>();
           for (Cabin cabin:cabins)
           {
               CabinModelList cabinModel=new CabinModelList();
               List<Bookings> bookings=bookingRepository.findBookingByCabinId(apiRequestModel.getStartDate(),apiRequestModel.getEndDate(),cabin.getCabinId(),BookingStatus.approved);
               cabinModel.setCabin(cabin);
               cabinModel.setBookings(bookings);
               cabinModelList.add(cabinModel);
           }
           if (cabinModelList.isEmpty())
           {
               return new ApiResponseModel<>(StatusResponse.not_found,null,"Cabin Avaliablility");
           }else {
               return new ApiResponseModel<>(StatusResponse.success, cabinModelList, "Cabin Avaliablility");
           }
    }

    public ApiResponseModel findAllOffice()
    {
        List<Office> officeList=officeRepository.findAll();
        List<String> offices=new ArrayList<>();
        for(Office office:officeList)
        {
            String officeId=office.getOfficeId();
            offices.add(officeId);
        }
        return new ApiResponseModel(StatusResponse.success,offices,"OfficeList");
    }



}
