package com.demo.service;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageFormat {

    public static void main(String[] args) {
        try {
            String xmlOutput1 = createXML();
            List<Map<String, Object>> xmlOutput = createXMLAmp("/Users/ssharma/Downloads/queryResult_excel.xls");
            String xml= createXMLStr(xmlOutput);
            System.out.println(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>>  createXMLAmp(String excelFilePath) throws Exception {
        // Read Excel file
        FileInputStream fis = new FileInputStream(new File(excelFilePath));
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);
        List<Map<String, Object>> shipments = new ArrayList<>();

        String tempShip="";
        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
            Row row = sheet.getRow(i);
            Map<String, Object> shipment2 = new HashMap<>();
            if (!tempShip.equals(row.getCell(1).getStringCellValue())) {
                shipment2.put("ShipmentNo", row.getCell(1).getStringCellValue());
                shipment2.put("ShipNode", row.getCell(2).getStringCellValue());
                ArrayList<Map<String, Object>> shipmentLines =  new ArrayList<Map<String, Object>>();
                for (int j = 1; j <= sheet.getLastRowNum(); j++) { // Skip header row
                    Row row1 = sheet.getRow(j);

                    if ( row.getCell(1).getStringCellValue().equals(row1.getCell(1).getStringCellValue())) {
                       // System.out.println("ItemID  : " + row1.getCell(3).getStringCellValue());
                        shipmentLines.add(Map.of("ItemID", row1.getCell(3).getStringCellValue(), "ShipmentLineNo", row1.getCell(4).getStringCellValue()));
                        shipment2.put("ShipmentLines", shipmentLines);
                    }
                }
                shipments.add(shipment2);
                tempShip= row.getCell(1).getStringCellValue();
            }
        }
        return  shipments;

    }
        public static String createXMLStr(List<Map<String, Object>> xmlOutput) throws Exception {

            // Create a StringBuffer to build the XML
            StringBuffer xml = new StringBuffer();
        List list = xmlOutput.stream().collect(Collectors.toCollection(ArrayList::new));


        for (Object   record: list)
        {
            HashMap map = (HashMap) record;
            String shipmentno =  map.get("ShipmentNo").toString();
            String shipNode =  map.get("ShipNode").toString();
            // Start the XML document and root element

            xml.append("<Shipment ShipmentNo=\""+shipmentno.replace("'","")+ "\" ShipNode=\""+shipNode.replace("'","")+"\" SellerOrganizationCode=\"BL_DOTCOM\">\n");
            // Start the ShipmentLines element
            xml.append("    <ShipmentLines>\n");
            ArrayList shipmentLines =(ArrayList) map.get("ShipmentLines");
            for (Object shipmentLineObj:   shipmentLines) {
                Map map1= (Map) shipmentLineObj;
                String ItemID = (String) map1.get("ItemID");
                String ShipmentLineNo = (String) map1.get("ShipmentLineNo");
                // Add first ShipmentLine
                xml.append("        <ShipmentLine Action=\"CANCELED\" ItemID=\""+ItemID.replace("'","")+"\" ShipmentLineNo=\""+ShipmentLineNo.replace("'","")+"\" UnitOfMeasure=\"EA\" Quantity=\"1\" CancelReasonCode=\"ChangedMind\"/>\n");

            }
            // End the ShipmentLines element
            xml.append("    </ShipmentLines>\n");
            // End the root element
            xml.append("</Shipment>\n");
        }

        // Output the generated XML
      //  System.out.println(xml.toString());
        return xml.toString();
    }

    public static String createXML1(String excelFilePath) throws Exception {
        // Create a new Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Create root element
        Element rootElement = document.createElement("Shipments");
        document.appendChild(rootElement);

        // Read Excel file
        FileInputStream fis = new FileInputStream(new File("/Users/ssharma/Downloads/queryResult_excel1.xls"));
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);
        String tempShip="";
        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
            Row row = sheet.getRow(i);
            Element shipmentElement = document.createElement("Shipment");
            Element shipmentLinesElement = document.createElement("ShipmentLines");

            if (!tempShip.equals(row.getCell(1).getStringCellValue())) {

                    shipmentElement.setAttribute("ShipmentNo",   row.getCell(1).getStringCellValue());
                    shipmentElement.setAttribute("ShipNode",  row.getCell(2).getStringCellValue());
                    shipmentElement.setAttribute("SellerOrganizationCode", "BL_DOTCOM");
                    rootElement.appendChild(shipmentElement);


                }
                Element shipmentLineElement = document.createElement("ShipmentLine");
                shipmentLineElement.setAttribute("Action", "CANCELED");
                shipmentLineElement.setAttribute("ItemID",  row.getCell(3).getStringCellValue()) ;
                shipmentLineElement.setAttribute("ShipmentLineNo",  String.valueOf(row.getCell(4).getNumericCellValue()));
                shipmentLineElement.setAttribute("UnitOfMeasure", "EA");
                shipmentLineElement.setAttribute("Quantity", "1");
                shipmentLineElement.setAttribute("CancelReasonCode", "ChangedMind");
                shipmentLinesElement.appendChild(shipmentLineElement);
                shipmentElement.appendChild(shipmentLinesElement);
                tempShip = row.getCell(1).getStringCellValue();


        }

        workbook.close();
        fis.close();

        // Convert Document to XML String
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));

        return writer.toString();
    }

    public static String createXML() throws Exception {
        // Create a new Document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        // Create Shipment element with attributes
        Element shipmentElement = document.createElement("Shipment");
        shipmentElement.setAttribute("ShipmentNo", "102775565");
        shipmentElement.setAttribute("ShipNode", "49");
        shipmentElement.setAttribute("SellerOrganizationCode", "BL_DOTCOM");
        document.appendChild(shipmentElement);

        // Create ShipmentLines element
        Element shipmentLinesElement = document.createElement("ShipmentLines");
        shipmentElement.appendChild(shipmentLinesElement);

        // Create ShipmentLine element with attributes
        Element shipmentLineElement = document.createElement("ShipmentLine");
        shipmentLineElement.setAttribute("Action", "CANCELED");
        shipmentLineElement.setAttribute("ItemID", "810795065");
        shipmentLineElement.setAttribute("ShipmentLineNo", "1");
        shipmentLineElement.setAttribute("UnitOfMeasure", "EA");
        shipmentLineElement.setAttribute("Quantity", "1");
        shipmentLineElement.setAttribute("CancelReasonCode", "ChangedMind");
        shipmentLinesElement.appendChild(shipmentLineElement);

        // Convert Document to XML String
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));

        return writer.toString();
    }

}
