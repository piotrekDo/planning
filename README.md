# Planning App

Planning is a rest service enabling managing carriers, their trucks, drivers as much as tautliners which can be also
organization's property. Initial version of this tool allows you to add, edit, delete and get all of the data. You can
couple trucks with drivers and tautliners. Coupling drivers with trucks is only available within the same carrier.
Tautliners can be shared between different carriers if being organization's property. Provided with initializer, with H2
database. Future realises will deliver security, enabling user login and authentication to all actions. Next I wish to
implement logging service showing all coupling actions done and by whom.

> *Security was provided since update 2.0.0*

All driver's names were generated with benedictcumberbatchgenerator.tumblr.com


> **Please familiarize with Swagger at its standard end-point**
>> */swagger-ui/*  
> > *at this point swagger offers authorization, however bearer token should be obtained with another http client e.g. Postman*

## Version Log

- 2.0.0 Spring security added. Login required to use application.
- 1.0.1 Posting new drivers and trucks requires now carrier's SAP instead of carrier's ID. Plus minor fixes.
- 1.0.0 Initial version introducing basic CRUD operations and coupling trucks with drivers and tautliners.

## Security

Since update 2.0.0 Spring Security was introduced. Security is handled by JWT tokens obtained at login. Access tokens
are valid for 10 hours, <u>**refresh tokens**</u> are generated as well, but <u>**not supported yet**</u>
> All user's passwords are encrypted.  
> Username must be unique  
> User email must be unique
> All new users are registered with USER as default role. Different roles can be added by ADMIN.

Authorization is based on roles. Currently, three roles exists:

- USER -has permission to view, edit and add new data, except other users.
- MODERATOR -has permission to delete data except other users
- ADMIN -has permission to register new users, delete users and manage their roles.

Due to data nature of present application, open registration of new users is not allowed. Only ADMIN users are allowed
to do so. Program is pre-configured with one ADMIN user. Newly created account will have generated password, sent to
user email, password is not known to creator. We urge to change password as soon as account was generated!

### Login

#### */login*

> is the default end-point for login. Requires credentials of username and password

**post request**

        {
        "username" : "string",
        "userPassword" : "string"
        }

If everything went well, API will return JSON containing tokens:

        {
            "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEiLCJyb2xlcyI6WyJVU0VSIiwiTU9ERVJBVE9SIiwiQURNSU4iXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2xvZ2luIiwiZXhwIjoxNjYyMjc3MTk1fQ.GH78rE6tSJ2vwjVH8sjAywh6irUxsONWG_cDR5LlGv4",
            "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbjEiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvbG9naW4iLCJleHAiOjE2NjIzMjM5OTV9.4loHgX97dwGuupyug3T2TTM5wM-PrrLuzOZB6pZDypw"
        }

### Adding users

#### */users/save*

> allowed only to ADMIN users. Requires username and user email. Username and email mus be unique.

**post request**

        {
            "userEmail": "string",
            "username": "string"
        }

### Delete user

#### */users/delete-user*

> allowed only to ADMIN users. Requires username. Use with caution.

**delete request**

        {
            "username": "string"
        }

### Adding new role to user

#### */users/role/add-to-user*

> allowed only to ADMIN users. Requires username and role name.

**post request**

        {
            "roleName": "string",
            "username": "string"
        }

### Removing role from user

#### */users/role/remove-from-user*

> allowed only to ADMIN users. USER role cannot be removed. Requires username and role.

**post request**

        {
            "roleName": "string",
            "username": "string"
        } 

### Resetting user password

#### */users/password-reset-request*

Password reset requires providing credentials in form of username and user mail.

**post request**

        {
            "email": "string",
            "username": "string"
        }

Password reset token is then sent to user mail, its required to rest password. Token is valid for 30 minutes. After
obtaining token send **post request**

#### */users/password-change*

**post request**

        {
            "changePasswordToken": "string",
            "newPassword": "string",
            "username": "string"
        }

## Getting data

### Carrier

#### */carriers*

> is the default end-point for getting full data of carriers and their assets.

#### */carriers/all-short*

> allows to get handy information about carriers themselves without collecting full assets data.

#### */carriers/sap*

> allows to get carrier by its SAP number which is unique.

### Truck

#### */trucks*

> is the default end-point for getting full list of trucks available at all carriers.

#### */trucks/plates*

> allows to get information about specific truck by its plate numbers.

### Tautliner

#### */tautliners*

> is the default end-point for getting full list of tautliners available at all carriers and within organization.
> Additional parameter can be used to get only organization's trucks:
>> /tautliners?isXpo=true </br>
>
> parameter is false by default.

#### */tautliners/plates*

> allows to get information about specific truck by its plate numbers.

### Truck driver

#### */drivers*

> is the default end-point for getting full list of drivers available at all carriers.

#### */drivers/id*

> allows to get information about specific driver by **his database id**. Database Id should not be mistaken with
> id document such as a passport!

## Posting new data and validation rules

Program provides simple formatting, capitalizing words such as carrier's name or driver's full name as well as trucks
and tautliners plates to keep united form. Driver's telephone number will be separated with "-" eg. 111-111-111.

### Carrier

> SAP number
> > Has to be unique, consisting 6 digits, no letters, special characters nor separators.

> Name
> > in range form 3 to 100 characters.

> Origin
> > Representing just a carrier's origin, not full address. In range form 3 to 100 characters.

> Rate
>> Representing carrier's standard rate per km.

### Truck

When posting a new truck, carrier's SAP must be provided- trucks cannot exist without a carrier.

> Plates
>> Must be between 3 and 15 characters, starting with 2-3 letters. No blanks, or spaces/separators.
> > Plates are unique

> Mega /standard
>> All truck should be declared either as low-decks or standard trucks.

### Tautliner

When posting a tautliner, carrier's SAP must be provided. If its 0 or null, then tautliner will be considered as
organization's one with no specific carrier owning it. Such tautliner can be shared between all carriers.

> Xpo
>> Very important trait. All tautliners should be declared either as organization's ones or carrier's.

> Plates
>> Must be between 3 and 15 characters, starting with 2-3 letters. No blanks, or spaces/separators.
> > Plates are unique

> Technical inspection
>> defines inspection deadline. Should be placed in format yyy-MM-dd.

### Truck driver

When posting a new driver, carrier's SAP must be provided- drivers cannot exist without a carrier.

> Full name
>> field joining both first name and last name for simplicity reasons. Should be between 3 and 100 characters.

> Telephone number
>> Containing 9 digits only, don't put any separators- program will format it to united form.

> Driver's id document
>> Must start with 2-3 letters. No separators, between 8-9 characters.

## Updating data

Updating data takes place by sending an update object with null values for fields, we don't wish to update by put
method. For example for editing only driver's telephone number a simple json will be enough:

    {
        "tel": "777777777"
    }

resulting in response body of updated object:

    {
        "fullName": "Baggageclaim Cuckatoo",
        "tel": "777-777-777",
        "idDocument": "IDO098123"
    }

## Deleting data

### Carriers

When deleting carrier, all its trucks ad drivers will be deleted as well. Tautliners will be removed as well, if belong
to that specific carrier. Organization's tautliners however will be uncoupled and will remain within the program,
available to couple with another carrier.

### Truck

When deleting a truck it will uncouple its driver and tautliner. Both will remain within the program for the same
carrier.

### Driver

When deleting a driver, his truck will be uncoupled and will remain within the program.

### Tautliner

When deleting a tautliner, it's truck will be uncoupled and will remain within the program.

## Coupling

Program allows for easy coupling trucks with drivers and tautliners and switching organization's tautliners between
carriers. Trucks, drivers and tautliners can be easily coupled within the same carrier. Program will remove any bonds
between them. Switching tautliners between different carriers however requires uncoupling tautliner with truck first!

Coupling requires delvering special pair-object by **put** method.

> **Truck & tautliner**  
> */couple/truck-tautliner*
>> requires truck's and tautliner's unique plates

    {
        "tautliner": "string",
        "truck": "string"
    }

<br>

> **Truck & driver**  
> */couple/truck-driver*
>> requires truck's unique plates and driver's unique database id **not to be mistaken with document id!**

    {
        "driver": 0,
        "truck": "string"
    }

<br>

> **Switching organization's tautliner to another carrier**  
> */couple/tautliner-carrier*
>> requires carrier's unique sap number and tautliner's unique plates

    {
        "carrierSap": "string",
        "tautliner": "string"
    }

## Handling errors

Errors are presented as objects containging 3 major fields:
> **code** representing specific http error code

> **message** representing specific http error name

> **details** being a map in terms of java code

error messages will vary for each model, depending on its fields, e.g. error object for trucks driver:

    {
    "code": 400,
    "message": "Bad Request",
    "details": {
        "idDocument": [
            "ID document has to start with letters, no separators",
            "Driver's id should be 8 characters if passport, 9 if ID document, NO SEPARATORS"
            ],
        "fullName": [
            "Driver's name must be between 3 anc 100 characters"
            ],
        "tel": [
            "Driver's tel should be 9 digits, NO SEPARATORS"
            ]
        }
    }