package com.FacilitiesManager.Service;

import com.FacilitiesManager.Entity.*;
import com.FacilitiesManager.Entity.Enums.*;
import com.FacilitiesManager.Entity.Model.ApiResponseModel;
import com.FacilitiesManager.Entity.Model.CabinAvaliableModel;
import com.FacilitiesManager.Repository.*;
import jakarta.mail.MessagingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
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

    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private MailingService mailingService;


    public ApiResponseModel createCabinBookingRequest(CabinRequest cabinRequest, User user) throws MessagingException {
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
                cabinRequest.setRequestDate(new Date());
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

                String content=mailingService.createCabinRequestMail(cabinRequestUser);
                List<String> managers=userRepo.findEmailsByRoleAndOfficeId(AccessRole.manager,cabinRequestUser.getOfficeId());
                mailingService.sendMail(managers,"Cabin request inititation notification",content,cabinRequestUser.getUserId());

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
        List<Cabin> cabins=new ArrayList<>();
        List<Cabin> cabinList=new ArrayList<>();
        List<BookingModel> bookings;
        List<CabinRequestModel> cabinRequests;
        List<ReservationList> reservationList=new ArrayList<>();



        if(cabinAvaliableModel.getBookingValadity().equals(BookingValadity.single_day))
        {

            cabins=cabinRepository.findCabinByOfficeIdAvailableForBooking(cabinAvaliableModel.getOfficeId(),BookingValadity.single_day);

            reservationList=reservationRepo.findCabinReservationSingleDay(cabinAvaliableModel.getOfficeId(),cabinAvaliableModel.getValidFrom(),cabinAvaliableModel.getValidTill(),cabinAvaliableModel.getStartDate(),BookingStatus.approved);

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
            cabins=cabinRepository.findCabinByOfficeIdAvailableForBooking(cabinAvaliableModel.getOfficeId(),BookingValadity.multiple_day);
            bookings=bookingModelRepository.findBookingsMultipleDaysBetweenDates(cabinAvaliableModel.getStartDate(),
                    cabinAvaliableModel.getEndDate(),
                    cabinAvaliableModel.getOfficeId());
            cabinRequests=cabinRequestModelRepository.findCabinBookingRequestMultipleDay(cabinAvaliableModel.getStartDate(),
                    cabinAvaliableModel.getEndDate(),
                    cabinAvaliableModel.getOfficeId(),
                    BookingStatus.hold);
        }

        if(cabins.isEmpty())
        {
            return new ApiResponseModel<>(StatusResponse.not_found,null,"No Cabin found");
        }

        if(bookings!=null)
        {
            for(Cabin cabin:cabins)
            {
                for(CabinRequestModel cabinRequest:cabinRequests)
                {
                    if(cabin.getCabinId()==cabinRequest.getCabinId())
                    {
                        int requestCount= (int)cabinRequests.stream()
                                .filter(c -> c.getCabinId() == cabin.getCabinId())  // Filter by cabinId
                                .count();
                        if(requestCount<=1&& cabinAvaliableModel.getBookingType().equals("Allotment")) {
                            cabin.setStatus(CabinAvaiability.Available);
                            cabin.setMsg("Available");
                        }else {
                            cabin.setStatus(CabinAvaiability.Requested);
                            cabin.setMsg("Already Requested");
                        }
                    }
                }

                for(BookingModel booking:bookings)
                {
                    if(cabin.getCabinId()==booking.getCabinId())
                    {
                        cabin.setStatus(CabinAvaiability.Booked);
                        cabin.setMsg("Already Booked");
                    }
                }

                for(ReservationList reservation:reservationList)
                {
                    if(cabin.getCabinId()==reservation.getCabinId())
                    {
                        cabin.setStatus(CabinAvaiability.Reserved);
                        cabin.setMsg("Cabin Reserved");
                    }
                }

                if(cabin.getStatus()==null|| cabin.getStatus().equals(CabinAvaiability.Available))
                {
                    cabin.setStatus(CabinAvaiability.Available);
                    cabin.setMsg("Available");
                }

                if(cabin.getStatus().equals(CabinAvaiability.Reserved))
                {
                    cabin.setStatus(CabinAvaiability.Reserved);
                    cabin.setMsg("Cabin Reserved");
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
                cabin.setCabinName(cabinRequest.getCabinName());
                cabin.setCapacity(cabinRequest.getCapacity());
                cabin.setAppliances(cabinRequest.getAppliances());
                cabin.setBookingType(cabinRequest.getBookingType());
                cabin.setStatus(cabinRequest.getStatus());
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
                CabinAvaiability cabinAvaiability=CabinAvaiability.Available;
                if(cabinRequest.getBookingValadity().equals(BookingValadity.single_day))
                {
                    isCabinAvailable=bookingService.checkCabinAvabalitySingleDay(cabinRequest);
                    avability=bookingService.checkCabinRequestSingleDay(cabinRequest);
                }else{
                    isCabinAvailable=bookingService.checkCabinAvailabilityMultipleDay(cabinRequest);
                    avability=bookingService.checkCabinRequestMultipleDay(cabinRequest);
                }

                if(!isCabinAvailable)
                {
                    cabinAvaiability=CabinAvaiability.Booked;
                } else if(!avability)
                {
                    cabinAvaiability=CabinAvaiability.Requested;
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

    public ApiResponseModel getAllCabinHoldRequestDate(User user,Date startDate,Date endDate)
    {
        List<CabinRequest> cabinRequests=cabinRequestRepository.findCabinRequestByOfficeIdAndDateRange(BookingStatus.hold, user.getOfficeId(),startDate,endDate);
        List<CabinRequest> cabinRequestList=new ArrayList<>();
        if(cabinRequests!=null)
        {
            for(CabinRequest cabinRequest:cabinRequests)
            {
                boolean isCabinAvailable;
                boolean avability;
                CabinAvaiability cabinAvaiability=CabinAvaiability.Available;
                if(cabinRequest.getBookingValadity().equals(BookingValadity.single_day))
                {
                    isCabinAvailable=bookingService.checkCabinAvabalitySingleDay(cabinRequest);
                    avability=bookingService.checkCabinRequestSingleDay(cabinRequest);
                }else{
                    isCabinAvailable=bookingService.checkCabinAvailabilityMultipleDay(cabinRequest);
                    avability=bookingService.checkCabinRequestMultipleDay(cabinRequest);
                }

                if(!isCabinAvailable)
                {
                    cabinAvaiability=CabinAvaiability.Booked;
                } else if(!avability) {
                    cabinAvaiability=CabinAvaiability.Requested;
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

    public ApiResponseModel getAllCabinRequest(User user,BookingStatus status)
    {
        List<CabinRequest> cabinRequests;
        if(status.equals(BookingStatus.all))
        {
            cabinRequests=cabinRequestRepository.findCabinRequestByOffice(user.getOfficeId());
        }
        else {
            cabinRequests = cabinRequestRepository.findCabinRequestByOfficeId(status, user.getOfficeId());
        }
        if(cabinRequests!=null)
        {
            return new ApiResponseModel<>(StatusResponse.success,cabinRequests,"Cabin Request List");
        }
        else {
            return new ApiResponseModel<>(StatusResponse.not_found, null, "No Cabin request");
        }
    }

    public ApiResponseModel createCabinReservation(CabinRequest cabinRequest,User userRequest)
    {
        try {
            Optional<Cabin> optionalCabin = cabinRepository.findById(cabinRequest.getCabinId());
            Cabin cabin = optionalCabin.get();
            Optional<User> optionalUser = userRepo.findById(userRequest.getEmailId());
            User user = optionalUser.get();

            ReservationList reservationList = new ReservationList();
            reservationList.setCabinId(cabinRequest.getCabinId());
            reservationList.setCabinName(cabin.getCabinName());
            reservationList.setDate(cabinRequest.getStartDate());
            reservationList.setValidFrom(cabinRequest.getValidFrom());
            reservationList.setValidTill(cabinRequest.getValidTill());
            reservationList.setOfficeId(user.getOfficeId());
            reservationList.setStatus(BookingStatus.approved);
            reservationRepo.save(reservationList);
            return new ApiResponseModel<>(StatusResponse.success,null,"Cabin reserved");
        }catch (Exception e)
        {
            e.printStackTrace();
            return new ApiResponseModel<>(StatusResponse.failed,null,"Unable to reserve cabin");
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

    public ApiResponseModel deleteCabin(int cabinId)
    {
      Optional<Cabin> optionalCabin=cabinRepository.findById(cabinId);
        if(optionalCabin.isPresent())
        {
            cabinRepository.deleteById(cabinId);
            return new ApiResponseModel<>(StatusResponse.success,null,"Cabin deleted");
        }
        else {
            return new ApiResponseModel<>(StatusResponse.failed,null,"Unable to delete cabin");
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

    public byte[] generateExcel() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cabin Details");

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

        Row headerRow = sheet.createRow(0);

        String[] headings = {"Cabin Name", "Capacity", "Appliances", "Booking Validity", "Status"};

        for (int i = 0; i < headings.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headings[i]);
            cell.setCellStyle(headerCellStyle);
        }

        for (int i = 0; i < headings.length; i++) {
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}