# Planning App

Planning is a rest service enabling managing carriers, their trucks, drivers as much as tautliners which can be also
organization's property. Initial version of this tool allows you to add, edit, delete and get all of the data. You can
couple trucks with drivers and tautliners. Coupling drivers with trucks is only available within the same carrier.
Tautliners can be shared between different carriers if being organization's property. Provided with initializer, with H2
database. Future realises will deliver security, enabling user login and authentication to all actions. Next I wish to
implement logging service showing all coupling actions done and by whom.  

All driver's names were generated with benedictcumberbatchgenerator.tumblr.com

> **At current state initializer should be disabled before running all tests!**

> **Please familiarize with Swagger at its standard end-point**
>> */swagger-ui/*

## Version Log

- 1.0.0. Initial version introducing basic CRUD operations and coupling trucks with drivers and tautliners.

## Getting data

### Carrier

*/carriers*
> is the default end-point for getting full data of carriers and their assets.

*/carriers/all-short*
> allows to get handy information about carriers themself without collecting full assets data.

*/carriers/sap*
> allows to get carrier by it's SAP number which is unique.

### Truck

*/trucks*
> is the default end-point for getting full list of trucks available at all carriers.

*/trucks/plates*
> allows to get information about specific truck by its plate numbers.

### Tautliner

*/tautliners*
> is the default end-point for getting full list of tautliners available at all carriers and within organization.
> Additional parameter can be used to get only organization's trucks:
>> /tautliners?isXpo=true </br>
>
> parameter is false by default.

*/tautliners/plates*
> allows to get information about specific truck by its plate numbers.

### Truck driver

*/drivers*
> is the default end-point for getting full list of drivers available at all carriers.

*/drivers/id*
> allows to get information about specific driver by **his database id**. Database Id should not be mistaken with
> id document such as a passport!

## Posting new data and validation rules

Program provides simple formatting, capitalizing words such as carrier's name or driver's full name as well as truck and
tautliner plates to keep united form. Driver's telephone number will be separated with "-" eg. 111-111-111.

### Carrier

> SAP number
> > Has to be unique, consisting 6 digits, no letters.

> Name
> > in range form 3 to 100 characters.

> Origin
> > Representing just a carrier's origin, not full address. In range form 3 to 100 characters.

> Rate
>> Representing carrier's standard rade per km.

### Truck

When posting a new truck, carrier's id must be provided- trucks cannot exist without a carrier.

> Plates
>> Must be between 3 and 15 characters, starting with 2-3 letters. No blanks, or spaces/separators.

> Mega /standard
>> All truck should be declared either as low-decks or standard trucks.

### Tautliner

When posting a tautliner, carrier's id mus be provided. If id is 0 or null tautliner will be considered as
organization's one with no specific carrier owning it. Such tautliner can be shared between all carriers.

> Xpo
>> Very important trait. All tautliners should be declared either as organization's ones or carrier's.

> Plates
>> Must be between 3 and 15 characters, starting with 2-3 letters. No blanks, or spaces/separators.

> Technical inspection
>> defines inspection deadline. Should be placed in format yyy-MM-dd.

### Truck driver

When posting a new driver, carrier's id must be provided- drivers cannot exist withou a carrier.

> Full name
>> field joining both first name and last name for simplicity reasons. Should be between 3 and 100 characters.

> Telephone number
>> Containing 9 digits only, please don't put any separators. Program will format it to united form.

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

When deleting carrier, all its truck ad driver will be deleted as well. Tautliners will be removed as well if belong to
that specific carrier. Organization's tautliners will be uncoupled and remain within the program available to couple
with another carrier.

### Truck

When deleting a truck it will be removed for driver, it's driver and tautliner will be uncoupled and remain within the
program.

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