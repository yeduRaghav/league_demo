## Developed using  :
- Android Studio 4.4.1
- Android Emulator 30.3.5

## To Run :
 - There are two modules : `app` & `kotlin_app`.
 - All code is in `kotlin_app`.
 - Run `kotlin-app` module
 
## Architecture :
- MVVM is the architecture with additional abstraction for bussiness logic (see: UseCases) and NetowrkingLogic(see: Endpoints)
- Fully written in Kotlin 
- Coroutines, LiveData, Hilt(DependencyInjection)


## Testing :
There are unit tests for important bussiness logic & networking logic, mostly regarding data transformation.
More testing could be easily implemented, given the modularity & loose coupling between entities.
Interface based design & leveraging extension functions aid in building testable code.

## Highlights :
- FeedUseCase & LoginUseCase shows example of abstraction, sepration of concern & resuability for bussiness logic, keeping ViewModels clean.
- Endpoint implementation showcase how new api-endpoints can be easily added to the project & their responses be dealt in a centralised manner.(see data.network.endpoint.base)
- Model -> Domain -> View DTO transformations provide abstraction of implementational details between the layers.

## Improvements necessary :
- The project structure could be cleaned up.
- Only UserStory 1 & 2 are built, the rest nedes to be done.
