# order-management-system-demo

Demo application for order-management-system using spring microservices architecture

## Procedure

To run the order management microservices system, open six CMD windows (Windows) or six Terminal windows (MacOS, Linux) and arrange so you can view them conveniently.

 1. In each window, change to the directory where you cloned the demo.
 1. In the first window, build the application using `mvn clean package`. This would have
    generated file in 'target/order-management-service.1.0.jar`.
 1. In the same window run: `java -jar target/order-management-service.1.0.jar registration`
 1. Switch to the second window and run: `java -jar target/order-management-service.1.0.jar product-catalog-service`
 1. In the third window run: `java -jar target/order-management-service.1.0..jar pricing-service`
 1. In the fourth window run: `java -jar target/order-management-service.1.0..jar tax-service`
 1. In the fifth window run: `java -jar target/order-management-service.1.0..jar order-service`
 1. In the sixth window run: `java -jar target/order-management-service.1.0..jar billing-service`
 1. In the seventh window run: `java -jar target/order-management-service.1.0..jar mailing-service`
 1. In your favorite browser or postman, open the links for each and access:
You should see servers being registered in the log output of the first (registration) window.
As you interact you should logging in the second and third windows.

## PostMan scripts:
1. Open PostMan
2. Click on Import and select OrderManagementSystem.postman_collection from the cloned demo.

## Load Balancing:
 1. In a new window, run up a second account-server using HTTP port 2223:
     * `java -jar target/microservices-demo-1.2.0.RELEASE.jar accounts 2223`
 1. Allow it to register itself
 1. Kill the first account-server and see the web-server switch to using the new account-server - no loss of service.

