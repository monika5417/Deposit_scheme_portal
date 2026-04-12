package com.mpcz.deposit_scheme.backend.api.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mpcz.deposit_scheme.backend.api.constant.ApiResponseCode;
import com.mpcz.deposit_scheme.backend.api.constant.HttpCode;
import com.mpcz.deposit_scheme.backend.api.constant.ResponseMessage;
import com.mpcz.deposit_scheme.backend.api.constant.RestApiUrl;
import com.mpcz.deposit_scheme.backend.api.domain.DynamicQuery;
import com.mpcz.deposit_scheme.backend.api.dto.DynamicExcelRequest;
import com.mpcz.deposit_scheme.backend.api.dto.MisExcelData;
import com.mpcz.deposit_scheme.backend.api.dto.MisExcelHeader;
import com.mpcz.deposit_scheme.backend.api.exception.CircleException;
import com.mpcz.deposit_scheme.backend.api.exception.ConsumerApplicationDetailException;
import com.mpcz.deposit_scheme.backend.api.exception.DemandDetailException;
import com.mpcz.deposit_scheme.backend.api.exception.DistributionCenterException;
import com.mpcz.deposit_scheme.backend.api.exception.DivisionException;
import com.mpcz.deposit_scheme.backend.api.exception.MisExcelDataException;
import com.mpcz.deposit_scheme.backend.api.exception.PaymentHistoryException;
import com.mpcz.deposit_scheme.backend.api.exception.SubDivisionException;
import com.mpcz.deposit_scheme.backend.api.repository.DynamicQueryRepository;
import com.mpcz.deposit_scheme.backend.api.response.Response;
import com.mpcz.deposit_scheme.backend.api.services.MisExcelDataService;
import com.mpcz.deposit_scheme.backend.api.util.ExcelGenerator;
import com.mpcz.deposit_scheme.backend.api.util.ExcelGeneratorHeader;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "MisExcelDocumentController", description = "Rest api for Creating Excel Consumer Application Detail.")
@RestController
@RequestMapping(RestApiUrl.MIS_EXCEL_DETAIL_API)
public class MisExcelDocumentController {

	Logger LOG = LoggerFactory.getLogger(MisExcelDocumentController.class);

	@Autowired
	MisExcelDataService misExcelDataService;

	@ApiOperation(value = "Get Excel Of Consumer Payment Charges Details", notes = "Get Excel Of Payment Charges Details", response = Response.class, responseContainer = "List", tags = RestApiUrl.MIS_EXCEL_DETAIL_TAGS)
	@ApiResponses(value = {
			@ApiResponse(code = ApiResponseCode.SUCCESS_CODE, message = ResponseMessage.RECORD_INSERTED_MESSAGE),
			@ApiResponse(code = ApiResponseCode.FORM_VALIDATION_ERROR, message = ResponseMessage.FORM_VALIDATION_ERROR),
			@ApiResponse(code = ApiResponseCode.UNSUPPORTED_MEDIA_TYPE_CODE, message = ResponseMessage.UNSUPPORTED_MEDIA_TYPE),
			@ApiResponse(code = ApiResponseCode.BAD_REQUEST_CODE, message = ResponseMessage.BAD_REQUEST),
			@ApiResponse(code = ApiResponseCode.UNAUTHORIZED_CODE, message = ResponseMessage.UNAUTHORIZED),
			@ApiResponse(code = ApiResponseCode.FORBIDDEN_CODE, message = ResponseMessage.FORBIDDEN),
			@ApiResponse(code = ApiResponseCode.NOT_FOUND_CODE, message = ResponseMessage.NOT_FOUND) })
	@GetMapping(RestApiUrl.GET_PDF_URL)
	public void exportIntoExcelFile(HttpServletResponse response)
			throws IOException, ConsumerApplicationDetailException, MisExcelDataException, PaymentHistoryException,
			DistributionCenterException, SubDivisionException, DivisionException, CircleException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=consumerApplicationStatus" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<MisExcelData> misExcelDetails = misExcelDataService.findAllDetails();
		Double totalOfRegistrationCharges = misExcelDataService.totalOfRegistrationCharges(misExcelDetails);
		ExcelGenerator generator = new ExcelGenerator(misExcelDetails, totalOfRegistrationCharges);
		generator.generateExcelFile(response);

	}

	@ApiOperation(value = "Get Excel Of Consumer Demand  Details", notes = "Get Excel Of Demand Details", response = Response.class, responseContainer = "List", tags = RestApiUrl.MIS_EXCEL_DETAIL_TAGS)
	@ApiResponses(value = {
			@ApiResponse(code = ApiResponseCode.SUCCESS_CODE, message = ResponseMessage.RECORD_INSERTED_MESSAGE),
			@ApiResponse(code = ApiResponseCode.FORM_VALIDATION_ERROR, message = ResponseMessage.FORM_VALIDATION_ERROR),
			@ApiResponse(code = ApiResponseCode.UNSUPPORTED_MEDIA_TYPE_CODE, message = ResponseMessage.UNSUPPORTED_MEDIA_TYPE),
			@ApiResponse(code = ApiResponseCode.BAD_REQUEST_CODE, message = ResponseMessage.BAD_REQUEST),
			@ApiResponse(code = ApiResponseCode.UNAUTHORIZED_CODE, message = ResponseMessage.UNAUTHORIZED),
			@ApiResponse(code = ApiResponseCode.FORBIDDEN_CODE, message = ResponseMessage.FORBIDDEN),
			@ApiResponse(code = ApiResponseCode.NOT_FOUND_CODE, message = ResponseMessage.NOT_FOUND) })
	@GetMapping(RestApiUrl.GET_HEADER_URL)
	public void exportHeaderDetailsIntoExcelFile(HttpServletResponse response)
			throws IOException, ConsumerApplicationDetailException, MisExcelDataException, PaymentHistoryException,
			DistributionCenterException, SubDivisionException, DivisionException, CircleException,
			DemandDetailException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=consumerApplicationStatus" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<MisExcelHeader> misExcelHeaderDetails = misExcelDataService.findAllHeaderDetails();
		List<Double> listOfDemandChargesDetails = misExcelDataService
				.totalOfDemandChargesDetails(misExcelHeaderDetails);

		ExcelGeneratorHeader generator = new ExcelGeneratorHeader(misExcelHeaderDetails, listOfDemandChargesDetails);
		generator.generateExcelFile(response);

	}
//	@Autowired
//	private DynamicQueryRepository dynamicQueryRepository;

	private final DynamicQueryRepository dynamicQueryRepository;

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	MisExcelDocumentController(DynamicQueryRepository dynamicQueryRepository,
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.dynamicQueryRepository = dynamicQueryRepository;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@PostMapping("/generateExcel")
	public void generateExcel(@RequestBody Map<String, Object> map, HttpServletResponse response,
			HttpServletRequest request) throws ConsumerApplicationDetailException, IOException {

		if (!map.containsKey("queryName"))
			throw new ConsumerApplicationDetailException(
					new Response<>(HttpCode.NOT_ACCEPTABLE, "query name key should not be null"));
		DynamicQuery dynamicQuery = dynamicQueryRepository.findByQueryNameData(String.valueOf(map.get("queryName")))
				.orElseThrow(() -> new ConsumerApplicationDetailException(
						new Response<>(HttpCode.NULL_OBJECT, "Query Not Found in Database")));

		Map<String, Object> params = new HashMap<>();
		params.put("authenticationId", map.get("authenticationId"));
		params.put("oneMonthBackDate", map.get("oneMonthBackDate"));

		List<Map<String, Object>> contractorData = namedParameterJdbcTemplate.queryForList(dynamicQuery.getQueryText(),
				params);

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("DSP Contractor Pendency Report");

		if (!contractorData.isEmpty()) {
			Row rowHeader = sheet.createRow(0); // create row in sheet
			int col = 0;
//		create coloum and add the coloumn name in it
			for (String key : contractorData.get(0).keySet()) {
				rowHeader.createCell(col++).setCellValue(key);
			}

			int rowNum = 1;
			for (Map<String, Object> rowMap : contractorData) {
				Row row = sheet.createRow(rowNum++);
				col = 0;
				for (Object value : rowMap.values()) {
					Cell cell = row.createCell(col++);
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}

		} else {
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue("No Data Found");

		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=DSP_Contractor_Pendency_Report.xlsx");

		workbook.write(response.getOutputStream());
		workbook.close();

	}

	@PostMapping("/generateDynamicExcel")
	public void generateDynamicExcel(@RequestBody DynamicExcelRequest dynamicExcelRequest, HttpServletRequest request,
			HttpServletResponse response) throws ConsumerApplicationDetailException, IOException {

		if (dynamicExcelRequest.getQueryName() == null) {
			throw new ConsumerApplicationDetailException(new Response<>(HttpCode.NULL_OBJECT, "Query Name is null"));
		}

		DynamicQuery dynamicQuery = dynamicQueryRepository
				.findByQueryNameData(dynamicExcelRequest.getQueryName().trim())
				.orElseThrow(() -> new ConsumerApplicationDetailException(new Response<>(HttpCode.NULL_OBJECT,
						"Query not found in database" + dynamicExcelRequest.getQueryName())));

		Map<String, Object> params = Objects.nonNull(dynamicExcelRequest.getParams()) ? dynamicExcelRequest.getParams()
				: new HashMap<>();

		List<Map<String, Object>> dbQueryData = namedParameterJdbcTemplate.queryForList(dynamicQuery.getQueryText(),
				params);

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(dynamicExcelRequest.getSheetName());
		String fileName = dynamicExcelRequest.getFileName();

		if (!dbQueryData.isEmpty()) {
			Row rowHeader = sheet.createRow(0); // create row in sheet
			int col = 0;
//		create coloum and add the coloumn name in it
			for (String key : dbQueryData.get(0).keySet()) {
				rowHeader.createCell(col++).setCellValue(key);
			}

			int rowNum = 1;
			for (Map<String, Object> rowMap : dbQueryData) {
				Row row = sheet.createRow(rowNum++);
				col = 0;
				for (Object value : rowMap.values()) {
					Cell cell = row.createCell(col++);
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}

		} else {
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue("No Data Found");

		}

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		workbook.write(response.getOutputStream());
		workbook.close();
	}

}
