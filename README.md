# Modget Create
This tool helps to create [Modget Manifests](https://github.com/ReviversMC/modget-manifests), 
which is used by the mod [Modget](https://github.com/ReviversMC/modget-minecraft). Modget Create (or MGC for short)
is **NOT** perfect, but will aid greatly in the amount of effort needed to contribute. 

## Usage
First off, thank you so much for wanting to contribute to Modget in the form of manifests. 
To get started, start by downloading the latest .jar file in [releases](https://github.com/ReviversMC/modget-create/releases).
```
Important Note: MGC is currently in development, and is only partially complete.
Join us on Discord to learn how you can help to beta test MGC!
```

Next up, ensure that you have Java 11 or higher on your computer. Launch the .jar file in your terminal/command prompt like this:
`java -jar </path/to/jar>`.

Once MGC starts up, you can view all MGC commands by using the `?` command.

## GitHub access
MGC has a feature where it will help you automatically PR your changes. However, this requires the access of your GitHub via oAuth to do so. 
With your authorization, MGC will:
- Fork [Modget Manifests](https://github.com/ReviversMC/modget-manifests)
- Create a new branch for the manifest
- Push all changes to the branch
- Open a PR to the main [Modget Manifests](https://github.com/ReviversMC/modget-manifests) repo

# Compiling from source
If you want to compile the file from source, it is really easy to do so! Open your terminal/command prompt and enter these few commands.

```
git clone https://github.com/ReviversMC/modget-create.git
cd modget-create
```

On Windows, continue with this command.
```
gradlew.bat :modget-create-cli:shadowJar
```

On macOS or Linux distros, use these commands instead.
```
chmod +x gradlew
./gradlew :modget-create-:cli:shadowJar
```

Finally, to launch modget-create: 
```
cd build
cd libs
java -jar <name/of/jar>
```

## Community
[![Discord chat](https://img.shields.io/badge/chat%20on-discord-7289DA?logo=discord&logoColor=white)](https://discord.gg/6bTGYFppfz)

We have an [official Discord community](https://discord.gg/6bTGYFppfz) for all of our projects. By joining, you can:
- Get installation help and technical support with all of our mods
- Be notified of the latest developments as they happen
- Get involved and collaborate with the rest of our team
- ... and just hang out with the rest of our community.


## Contributors
<a href="https://github.com/ReviversMC/modget-create/graphs/contributors">
  <img height="40em" src="https://contrib.rocks/image?repo=ReviversMC/modget-create" />
</a>