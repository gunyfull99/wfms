package com.wfms.service.impl;

import com.wfms.Dto.ResponseValidateUserDTO;
import com.wfms.Dto.UserValidateDto;
import com.wfms.entity.Roles;
import com.wfms.entity.Users;
import com.wfms.repository.RoleRepository;
import com.wfms.repository.UsersRepository;
import com.wfms.service.ProfileService;
import com.wfms.utils.DataUtils;
import com.wfms.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.wfms.utils.Constants.PATH_TEMPLATE;

@Service
@Slf4j
public class ProfileTemplateImpl implements ProfileService {
    @Autowired
    ResourcePatternResolver resourcePatternResolver;
    @Autowired
    private ExcelUtils excelUtils;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Resource getFileTemplate(String fileName) {
        try {
            Assert.notNull(fileName, "Tên file không được để trống");
            Resource[] listResource = resourcePatternResolver.getResources(PATH_TEMPLATE + "*");
            Optional<Resource> resourceOptional = Arrays.stream(listResource).filter
                    (resource -> fileName.equalsIgnoreCase(resource.getFilename())).findFirst();
            Assert.isTrue(resourceOptional.isPresent(), "Không thể tìm thấy file " + fileName);
            return  resourceOptional.get();
          //  return resourceOptional.orElseThrow(() -> new IllegalArgumentException("Không thể tìm thấy file " + fileName));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
           throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public byte[] updateFileTemplate(Resource file) throws IOException {
        List<Users> listUser = usersRepository.findUserNotInProject();
        Assert.isTrue(DataUtils.listNotNullOrEmpty(listUser), "Không có nhân viên đang trống việc để thêm vào dự án");
        Map<String, List<String>> mapData = new HashMap<>();
        SimpleDateFormat dateTimeFormatter =  new SimpleDateFormat("yyyy-MM-dd");

        List<String> userList = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        List<String> dateOfBirthList = new ArrayList<>();
        List<String> sexList = new ArrayList<>();
        listUser.forEach(i-> {
            userList.add(i.getId().toString() + "-" + i.getFullName() + "-" + i.getJobTitle());
        });
        listUser.forEach(i-> {
            userIdList.add(i.getId().toString());
        });
        listUser.forEach(i-> sexList.add(String.valueOf(i.getGender())));
        listUser.forEach(i-> dateOfBirthList.add(dateTimeFormatter.format(i.getBirthDay())));

        List<String> usernameList = listUser.stream().map(Users::getUsername).collect(Collectors.toList());
        List<String> addressList = listUser.stream().map(Users::getAddress).collect(Collectors.toList());
        List<String> emailAddressList = listUser.stream().map(Users::getEmailAddress).collect(Collectors.toList());
        List<String> phoneNumberList = listUser.stream().map(Users::getPhone).collect(Collectors.toList());
        List<String> jobTitleList = listUser.stream().map(Users::getJobTitle).collect(Collectors.toList());

        mapData.put("user", userList);
        mapData.put("username", usernameList);
        mapData.put("sex", sexList);
        mapData.put("address", addressList);
        mapData.put("emailAddress", emailAddressList);
        mapData.put("phoneNumber", phoneNumberList);
        mapData.put("dateOfBirth", dateOfBirthList);
        mapData.put("jobTitle", jobTitleList);
        mapData.put("userId", userIdList);

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        CellStyle headerStyle = ExcelUtils.createBorderedStyle(workbook);
        headerStyle.setFont(ExcelUtils.createBoldStyle(workbook));
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        Sheet sheet = workbook.getSheet("Note");
        Row row;
        Name namedRange;
        String colLetter;
        String reference;
        List<String> keyStrings = Arrays.asList("user", "username", "sex", "address", "emailAddress", "phoneNumber", "dateOfBirth", "jobTitle","userId");
        int c = 0;
        //put the data in
        for (String key : keyStrings) {
            int r = 0;
            row = sheet.getRow(r);
            if (row == null) row = sheet.createRow(r);
            r++;
            row.createCell(c).setCellValue(key);
            row.getCell(c).setCellStyle(headerStyle);
            List<String> items = mapData.get(key);
            for (String item : items) {
                row = sheet.getRow(r);
                if (row == null) row = sheet.createRow(r);
                r++;
                row.createCell(c).setCellValue(item);
            }
            if ("user".contains(key)){
                colLetter = CellReference.convertNumToColString(c);
                namedRange = workbook.createName();
                namedRange.setNameName(key);
                reference = "Note!$" + colLetter + "$2:$" + colLetter + "$" + r;
                namedRange.setRefersToFormula(reference);
            }
            sheet.autoSizeColumn(c);
            c++;
        }
        colLetter = CellReference.convertNumToColString((c - 1));
        namedRange = workbook.createName();
        namedRange.setNameName("template");
        reference = "Note!$A$1:$" + colLetter + "$1";
        namedRange.setRefersToFormula(reference);
        sheet = workbook.getSheetAt(0);
        sheet.setActiveCell(new CellAddress("A2"));
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("user");
        CellRangeAddressList addressSheetList = new CellRangeAddressList(1, 100, 0, 0);
        DataValidation validation = dvHelper.createValidation(dvConstraint, addressSheetList);
        sheet.addValidationData(validation);

        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        try {
            workbook.write(ms);
            ms.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return ms.toByteArray();
    }

    @Override
    public byte[] createTemplateUser(Resource file) throws IOException {
        List<Roles> roles = roleRepository.findAll();
        List<String> rolesName = roles.stream().map(Roles::getName).collect(Collectors.toList());
        List<String> gender =  Arrays.asList("Nam","Nữ");
        Map<String, List<String>> mapData = new HashMap<>();
        mapData.put("roles",rolesName);
        mapData.put("gender",gender);
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        CellStyle headerStyle = ExcelUtils.createBorderedStyle(workbook);
        headerStyle.setFont(ExcelUtils.createBoldStyle(workbook));
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        Sheet sheet = workbook.createSheet("Note");
        Row row;
        Name namedRange;
        String colLetter;
        String reference;
        List<String> keyStrings = Arrays.asList("username", "gender", "address", "emailAddress", "phone", "birthDay", "fullName", "roles","jobTitle");
        int c = 0;
        //put the data in
        for (String key : keyStrings) {
            int r = 0;
            row = sheet.getRow(r);
            if (row == null) row = sheet.createRow(r);
            r++;
            row.createCell(c).setCellValue(key);
            row.getCell(c).setCellStyle(headerStyle);
            List<String> items = mapData.get(key);
            if (items!=null){
                for (String item : items) {
                    row = sheet.getRow(r);
                    if (row == null) row = sheet.createRow(r);
                    r++;
                    row.createCell(c).setCellValue(item);
                }
            }
            if ("roles".contains(key) || "gender".contains(key)){
                colLetter = CellReference.convertNumToColString(c);
                namedRange = workbook.createName();
                namedRange.setNameName(key);
                reference = "Note!$" + colLetter + "$2:$" + colLetter + "$" + r;
                namedRange.setRefersToFormula(reference);
            }
            sheet.autoSizeColumn(c);
            c++;
        }
        colLetter = CellReference.convertNumToColString((c - 1));
        namedRange = workbook.createName();
        namedRange.setNameName("template1");
        reference = "Note!$A$1:$" + colLetter + "$1";
        namedRange.setRefersToFormula(reference);
        sheet = workbook.getSheetAt(0);
        sheet.setActiveCell(new CellAddress("A2"));
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("roles");
        CellRangeAddressList addressSheetList = new CellRangeAddressList(1, 100, 7, 7);
        DataValidation validation = dvHelper.createValidation(dvConstraint, addressSheetList);
        sheet.addValidationData(validation);

        dvConstraint = dvHelper.createFormulaListConstraint("gender");
        addressSheetList = new CellRangeAddressList(1, 100, 1, 1);
        validation = dvHelper.createValidation(dvConstraint, addressSheetList);
        sheet.addValidationData(validation);

        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        try {
            workbook.write(ms);
            ms.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return ms.toByteArray();
    }

    @Override
    public ResponseValidateUserDTO validateFileCreateUser(MultipartFile files) {
        Assert.notNull(files,"Không tìm thấy file gửi lên");
        List<UserValidateDto> usersList = new ArrayList<>();
        List<Users>users=usersRepository.findAll();
        List<String> username = users.stream().map(Users::getUsername).collect(Collectors.toList());
        List<String> email = users.stream().map(Users::getEmailAddress).collect(Collectors.toList());
        int numberTotal = 0;
        int numberItemFail = 0;
        int numberItemPass = 0;
        // chỉ thực hiện validate định dạng file, ko validate tên file
        String fileExtention = Objects.requireNonNull(files.getOriginalFilename()).split(Pattern.quote("."))[1];
        Assert.isTrue(("xlsx".equals(fileExtention) || "xls".equals(fileExtention)),"Định dạng file không đúng, chỉ chấp nhận định dạng xlsx, xls");
        DataFormatter formatter = new DataFormatter();
        try(Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(files.getBytes()))) {
            List<String> header = List.of("username", "gender", "address", "emailAddress", "phone", "birthDay", "fullName", "roles","jobTitle");
            Sheet sheet = workbook.getSheetAt(0);
            // thực hiện validate định dạng file
            Row rowHeader = sheet.getRow(0);
            int colNUmber = rowHeader.getPhysicalNumberOfCells();
            for (int i=0;i<colNUmber;i++){
                Assert.isTrue(header.get(i).contains(rowHeader.getCell(i).getStringCellValue()),"Định dạng file không đúng, tải lại định dạng hoặc kiểm tra lại file của bạn");
            }
            Iterator<Row> item = sheet.iterator();
            item.next();
            while (item.hasNext()){
                numberTotal++;
                Row row = item.next();
                String userName = row.getCell(0).getStringCellValue();
                String gender = formatter.formatCellValue(row.getCell(1));
                String address = formatter.formatCellValue(row.getCell(2));
                String emailAddress = formatter.formatCellValue(row.getCell(3));
                String phone = formatter.formatCellValue(row.getCell(4));
                String birthDay = formatter.formatCellValue(row.getCell(5));
                String fullName = formatter.formatCellValue(row.getCell(6));
                String roles = formatter.formatCellValue(row.getCell(7));
                String jobTitle = formatter.formatCellValue(row.getCell(8));
                UserValidateDto usersDto =  UserValidateDto.builder().username(userName).gender(gender).address(address)
                                                        .emailAddress(emailAddress).phone(phone).birthDay(birthDay)
                                                        .fullName(fullName).role(roles).jobTitle(jobTitle).build();
                String message = validateUserExist(username,email,usersDto);
                if (message != null){
                    usersDto.setMessageValidate(message);
                }else {
                    usersDto = validateField(usersDto);
                }
                if (DataUtils.notNullOrEmpty(usersDto.getMessageValidate())){
                    numberItemFail++;
                }else {
                    numberItemPass++;
                }
                usersList.add(usersDto);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseValidateUserDTO.builder().listUserValidate(usersList)
                                                        .total(numberTotal)
                                                        .totalFail(numberItemFail)
                                                        .totalPass(numberItemPass).build();
    }
    private UserValidateDto validateField(UserValidateDto usersDto){
        UserValidateDto userValidateDto = new UserValidateDto();
        DataFormatter formatter = new DataFormatter();
        BeanUtils.copyProperties(usersDto,userValidateDto);
        StringBuilder message = new StringBuilder();
        //valdate trường null;
        if (!DataUtils.notNull(usersDto.getUsername())){
            message.append("Username không được để trống ");
        }
        if (!DataUtils.notNull(usersDto.getAddress())){
            message.append(", Địa chỉ không được để trống ");
        }
        if (!DataUtils.notNull(usersDto.getRole())){
            message.append(", Quyền nhân viên không được để trống ");
        }else {
            Roles roles = roleRepository.getRoleByRoleName(usersDto.getRole());
            if (DataUtils.notNull(roles)){
                userValidateDto.setRoleId(roles.getId());
            }else {
                message.append(", Công việc ").append(usersDto.getRole()).append(" không đúng");
            }
        }
        if (!DataUtils.notNull(usersDto.getFullName())){
            message.append(", Tên nhân viên không được để trống ");
        }

        if (!DataUtils.notNull(usersDto.getEmailAddress())){
            message.append(", Địa chỉ email không được để trống ");
        }else {
            Pattern pattern = Pattern.compile("^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
            Matcher matcher = pattern.matcher(usersDto.getEmailAddress());
            if(!matcher.find()) message.append(", Email không hợp lệ");
        }
        if (!DataUtils.notNull(usersDto.getBirthDay())){
            message.append(", Ngày sinh không được để trống ");
        }else {
            if (DataUtils.convertStringToDate(usersDto.getBirthDay(),"yyyy/MM/dd")==null){
                message.append(", Ngày sinh điền dưới dạng yyyy/MM/dd");
            }
        }
        if (!DataUtils.notNull(usersDto.getGender())){
            message.append(", Giới tính không được để trống ");
        }else {
            // thực hiện validate giới tính
            if (List.of("Nam","Nữ").contains(usersDto.getGender())){
                userValidateDto.setGenderCode("Nam".equals(usersDto.getGender()) ? 1 : 0);
            }else {
                message.append(", Giới tính").append(usersDto.getGender()).append(" không đúng");
            }
        }
        if (!DataUtils.notNull(usersDto.getPhone())){
            message.append(", Số điện thoại không được để trống ");
        }else {

            if (!DataUtils.isInteger(usersDto.getPhone())){
                message.append(", Số điện thoại không đúng ");
            }else {
                Pattern pattern = Pattern.compile("^[0][0-9]{9}$");
                Matcher matcher = pattern.matcher(usersDto.getPhone());
                if(!matcher.find()) message.append(", Số điện thoại phải là 10 chữ số bắt đầu từ số 0 ");
            }
        }
        if (!DataUtils.notNull(usersDto.getJobTitle())){
            message.append(", Loại nhân viên không được để trống ");
        }
        String messageAll = message.toString().startsWith(",") ? message.toString().replaceFirst(",", "") : message.toString();
        userValidateDto.setMessageValidate(messageAll);
        return userValidateDto;
    }
    private String validateUserExist( List<String> username,List<String> email,UserValidateDto usersDto){
        if (username.contains(usersDto.getUsername().trim()) || email.contains(usersDto.getEmailAddress().trim())){
            return "Username hoặc email đã được đăng ký trước đó";
        }
        return null;
    }
}
