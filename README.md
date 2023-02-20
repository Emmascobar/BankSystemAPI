
# BANKING SYSTEM API

An API is a programming interface designed exclusively for the use of clients who require a careful treatment of data and optimal management of their systems.
With the Banking System API, we offer a careful treatment of the database that structures the banking like an entity, speeding up, in a detailed manner, the registration of operations,optimizing data and generating transactions that keep, always safe, the financial bank system.

Banking System API is an application developed under the REST API architecture criteria, which allows optimal integration with external servers and database. It is designed to be used mainly by bank administrators, containing access routes for account holders, as well as third parties.
## How to configure
1) Banking System used SQL database, so required configure an instance and schema in MySQL with the name "Banking_System", this will be database saving routes.
2) Run the program to give permission to the controller routes to make changes.
3) Controller routes can be tested using POSTMAN or any other HTTP request and test programme (At the final of this chapter you can find a link with JSON raw information to facilitate test it).
## Instances & Methods
Through the correct use of the API, you will be able to:
#### Create USERS: Administrators, holder accounts and third parties
#### Create BANK ACCOUNTS: Checking, savings and credit cards. 
#### Checking accounts:
- When creating it, depending on the client's age (24 years), a student account is generated or not,  automatically. The Student-Checking Account will not have monthly maintenance.

#### Penalty fee:
- It is configured to apply when the balance of the accounts is below the default minimum balance.

#### Interest Rate:
- Default applied annually to savings accounts and monthly to credit card accounts.
## USERS Permissions

#### ADMINS permissions:
- Admins can create other administrators, account holders and third parties. They can also check accounts, update balances and delete any other accounts.

#### ACCOUNT HOLDERS permissions:
- Accounts holder can make transfers and consult only their accounts.

#### THIRD-PARTY permissions:
- TP can send and receive money, across transfers, to the others accounts.

#### ALL USERS:
- All users have a SecretKey that is implemented throughout the API as a security measure.
## Main technologies used:
- Java / Spring / SpringBoot : to write the API code by using the libraries provided by both frameworks.
- Unit testing : to test the functionality of all repository routes and controllers.
- JPA: to map and relate the API with the database.
## API Structure Diagram:
- https://drive.google.com/file/d/1VZFj1-qXqRPVK6rS_7EtEFxR-0ZRpk5_/view?usp=share_link
## Information for Devs:
- The API code is written with the naming conventions, among others, we use PascalCase for the name of Models, and their homonyms in SQL with snake_case. It's suggested for future modifications follows the same convention to keep clean the project.
- Each method that calls a setter is accompanied by a comment with details about your implementation. The JSON file attached at the following link provides the raw data for a easily access, check, and modify routes on Postman body. (https://drive.google.com/file/d/10NCLJN8WaWKpbKv1mjc2stysk71kzolZ/view?usp=share_link)
## App done by:
- [Emmanuel Escobar](https://github.com/Emmascobar)
