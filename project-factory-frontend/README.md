# Frontend

## Project Setup

### Required Pre-requisites

1. [Node JS](https://nodejs.org/en/)
2. [Angular CLI](https://cli.angular.io/)



### Installing node in linux

Run the following commands for your respective distributions:

**Using Ubuntu**
`curl -sL https://deb.nodesource.com/setup_10.x | sudo -E bash -`
`sudo apt-get install -y nodejs`

**Using Debian, as root**
`curl -sL https://deb.nodesource.com/setup_10.x | bash `-
`apt-get install -y nodejs`

### Installing node in windows

**For 32-bit**
Download and install nodejs from : [Node JS v10.16.1 32-bit ](https://nodejs.org/download/release/v10.16.1/node-v10.16.1-x86.msi)

**For 64-bit**
Download and install nodejs from : [Node JS v10.16.1 64-bit ](https://nodejs.org/download/release/v10.16.1/node-v10.16.1-x64.msi)


#### Installing angular cli and project dependencies

Run the following commands:

> npm install -g @angular/cli@8.1.2


Once installed all the required pre-requisites, pull the latest code to the local machine. Once pulled the code, get into the project folder and run `npm install` to download the required dependencies from node package manager for the project.

### Source Code Formatting

- Use `npm run spotlessApply` to apply formatting for all the source files. While applying beautification, the command will list down the source files for which beautification has been applied.

- Next, use `npm run spotlessCheck` to verify whether the source files are beautified or not. If the source files are not beautified this command will list down those files with the error code.


### Building the Application

- Use `ng build` to build the application which will produce the bundles of source files grouped together and this command will not apply the production settings.

- Use `ng build --prod` to build the application with the production settings and it will do the following optimization features.
    - Ahead-of-Time (AOT) Compilation: pre-compiles Angular component templates.
    - Production mode: deploys the production environment which enables production mode.
    - Bundling: concatenates your many application and library files into a few bundles.
    - Minification: removes excess whitespace, comments, and optional tokens.
    - Uglification: rewrites code to use short, cryptic variable and function names.
    - Dead code elimination: removes unreferenced modules and much-unused code.

### Refer the [Debugging Settings](debugging-settings.md) for debugging the source code in VS code.
