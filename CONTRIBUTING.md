Hi! Thanks for taking time out of your day to contribute to this project. 
Before you start, just a few things that you might want to take note of. 

1. This project is written in Java. Therefore, contributions in Java are preferred over other languages (e.g. Kotlin).
2. Please name **do not** name your interfaces with the 'I' prefix, or your classes with the "Impl" suffix.
Try as much as possible to name your class/interface according to what it does. (e.g. "Command" (interface) and "HelpCommand" (impl)).
3. This project uses a few dependencies, most notably of which is "Dagger 2" (aka Dagger by Google). Use Dagger 2 to do DI as much as possible.
If you do not have much experience with "Dagger 2", you might find contributing a struggle.
4. When printing to the console, please try as much as possible to {@link colorize} your messages according to this structure.
    - Red = Error/Function cancellations.
    - Yellow = Warning
    - Green = Success/info  
5. Avoid including messages in modget-create-core. This is to allow for use in a GUI, coming soonTM.

Congrats! You finished reading this boring chunk of text. That is all of our contributing guidelines - thanks for helping in advance!