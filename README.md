Here is the content for your README.md file:

markdown
Copy
Edit
# H2 Database Integration for Order Cancellation

This repository is used for **H2 database** in both **memory** and **file** mode.

## Features
- Reads an **Excel file** containing order records.
- Converts the order data into **XML format** for shipment order cancellations.

## Technologies Used
- **H2 Database** (In-Memory & File Mode)
- **Java**
- **Apache POI** (for reading Excel files)
- **XML Processing**

## How It Works
1. Load order data from an Excel file.
2. Process and transform the data into shipment cancellation XML format.
3. Store the necessary details in the H2 database.

<Shipment ShipmentNo="102538237" ShipNode="4756" SellerOrganizationCode="BL_DOTCOM">
    <ShipmentLines>
        <ShipmentLine Action="CANCELED" ItemID="810758724" ShipmentLineNo="1" UnitOfMeasure="EA" Quantity="1" CancelReasonCode="ChangedMind"/>
    </ShipmentLines>
</Shipment>
