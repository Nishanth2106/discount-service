# Retail Website Discounts

This project implements discount calculations for a retail website based on given requirements.

## How to Run

### Prerequisites

Make sure you have the following installed:

- Java 8 or later
- Maven

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/Nishanth2106/discount-service.git
    ```

2. Navigate to the project directory:

    ```bash
    cd discount-service
    ```

### Running the Application

To run the application, execute the following Maven command:

```bash
mvn spring-boot:run
```

### Test the Api
````
curl --location 'http://localhost:6767/calculateNetPayableAmount' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=BF1C8F46F57CD25CC7A63FADB4AD7086' \
--data '{
    "billId": 123456789,
    "user": {
        "userId": 987654321,
        "userType": "EMPLOYEE",
        "registrationDate": "2023-01-15"
    },
    "items": [
        {
            "itemId": 1,
            "itemName": "Product 1",
            "itemType": "GROCERY",
            "itemPrice": 90
        },
        {
            "itemId": 2,
            "itemName": "Product 2",
            "itemType": "GROCERY",
            "itemPrice": 105
        }
    ]
}'
