DROP TABLE IF EXISTS TOOLS;  
CREATE TABLE TOOLS (  
    code VARCHAR(50) PRIMARY KEY,
    label VARCHAR(50) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    charge DECIMAL(6,2) NOT NULL,
    wdCharge BOOLEAN NOT NULL,
    weCharge BOOLEAN NOT NULL,
    hdCharge BOOLEAN NOT NULL,
    checkoutDate DATE NOT NULL,
    returnDate DATE NOT NULL,
    rentalDays INT NOT NULL,
    chargeableDays INT NOT NULL
);  