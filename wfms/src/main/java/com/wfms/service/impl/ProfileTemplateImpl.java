package com.wfms.service.impl;

import com.wfms.entity.Users;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
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

    @Override
    public Resource getFileTemplate(String fileName) {
        try {
            Assert.notNull(fileName, "Tên file không được để trống");
            Resource[] listResource = resourcePatternResolver.getResources(PATH_TEMPLATE + "*");
            Optional<Resource> resourceOptional = Arrays.stream(listResource).filter
                    (resource -> fileName.equalsIgnoreCase(resource.getFilename())).findFirst();
            return resourceOptional.orElseThrow(() -> new IllegalArgumentException("Không thể tìm thấy file " + fileName));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public byte[] updateFileTemplate(Resource file) throws IOException {
        List<Users> listUser = usersRepository.findUserNotInProject();
        Assert.isTrue(DataUtils.listNotNullOrEmpty(listUser), "Không có nhân viên đang trống việc để thêm vào dự án");
        Map<String, List<String>> mapData = new HashMap<>();
        SimpleDateFormat dateTimeFormatter =  new SimpleDateFormat("dd-MM-yyyy");

        List<String> userIdList = new ArrayList<>();
        List<String> dateOfBirthList = new ArrayList<>();
        List<String> sexList = new ArrayList<>();
        listUser.forEach(i-> userIdList.add(i.getId().toString()));
        listUser.forEach(i-> sexList.add(String.valueOf(i.getGender())));
        listUser.forEach(i-> dateOfBirthList.add(dateTimeFormatter.format(i.getBirthDay())));

        List<String> usernameList = listUser.stream().map(Users::getUsername).collect(Collectors.toList());
        List<String> addressList = listUser.stream().map(Users::getAddress).collect(Collectors.toList());
        List<String> emailAddressList = listUser.stream().map(Users::getEmailAddress).collect(Collectors.toList());
        List<String> phoneNumberList = listUser.stream().map(Users::getPhone).collect(Collectors.toList());
        List<String> jobTitleList = listUser.stream().map(Users::getJobTitle).collect(Collectors.toList());

        mapData.put("userId", userIdList);
        mapData.put("username", usernameList);
        mapData.put("sex", sexList);
        mapData.put("address", addressList);
        mapData.put("emailAddress", emailAddressList);
        mapData.put("phoneNumber", phoneNumberList);
        mapData.put("dateOfBirth", dateOfBirthList);
        mapData.put("jobTitle", jobTitleList);

        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        CellStyle headerStyle = ExcelUtils.createBorderedStyle(workbook);
        headerStyle.setFont(ExcelUtils.createBoldStyle(workbook));
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        Sheet sheet = workbook.getSheet("Note");
        Row row;
        Name namedRange;
        String colLetter;
        String reference;
        List<String> keyStrings = Arrays.asList("userId", "username", "sex", "address", "emailAddress", "phoneNumber", "dateOfBirth", "jobTitle");
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
            if ("userId".contains(key)){
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
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint("userId");
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
}
