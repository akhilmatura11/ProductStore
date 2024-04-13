**Android App**
This Android application displays Products i n a grid view and lets you add and edit them.

This project is built on MVVM and Clean Architecture.

Different layers are:

Data - The data for the application is managed in two different data sources.
LocalDataSource handles the persistant storage containing RoomDatabase and DAO objects.
RemoteDataSource handles the ApiInterface from where the call to get the products, create and update the products are made.
ProductRepositoryImpl - This class implements the ProductRepository interface (defined in domain layer).

Domain - The domain layer handles the data received in JSON format to convert into respective models, defined the ProductRepository to be used by viewModels for making API requests and retriving data.
Models
ProductRepository - This is where API request is initiaited, response gathered, saved in database and retrives data from database to pass data to viewModel following single source of truth comcept.

Presentation - The presentationn layer mainly comprises of UI related classes (Activity/Fragments/Adapters), and viewModel. No business logic is written in UI related classes. Any events coming from the user interaction are passed to viewModel to process the event, generate the states and then send it back to UI layer

Live data is used to observe the change in value of data objects and the changes will be reflected on the UI automatically thru data binding.

For dependency injection, we are using HILT. We are injecting Database, Network, Repository and ViewModels using the same.

We are implementing single activity principle, and fragments are navigated thru Navigation Host and navGraphs.

To fetch data from retrofit calls or save into database, we are using Kotlin coroutines.

To run the code

using Studio, immport the project using Android Studio
File -> New -> Project from Version Control
Let the gradle sync build, and once sync is successful, you can run the code on any device/emulator.
using apk shared - just install the apk on any device/emulator
To test the code


**GoLang (product-manager-backend)**

Backend is developed in GoLang with PostgreSQL.
host setup on localhost.
