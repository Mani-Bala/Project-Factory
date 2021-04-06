# Debugging Settings

## Steps to debug in VS code
1. Install the Chrome Debugger Extension
2. Create the `launch.json` (inside `.vscode` folder)
    
    Use the following code

 ```json
 {
    "version": "0.2.0",
    "configurations": [
      {
        "name": "Launch Chrome",
        "type": "chrome",
        "request": "launch",
        "url": "http://localhost:4200/#",
        "webRoot": "${workspaceFolder}"
      },
      {
        "name": "Attach Chrome",
        "type": "chrome",
        "request": "attach",
        "url": "http://localhost:4200/#",
        "webRoot": "${workspaceFolder}"
      },
      {
        "name": "Launch Chrome (Test)",
        "type": "chrome",
        "request": "launch",
        "url": "http://localhost:9876/debug.html",
        "webRoot": "${workspaceFolder}"
      },
      {
        "name": "Launch Chrome (E2E)",
        "type": "node",
        "request": "launch",
        "program": "${workspaceFolder}/node_modules/protractor/bin/protractor",
        "protocol": "inspector",
        "args": ["${workspaceFolder}/protractor.conf.js"]
      }
    ]
  }

 ```

 3. Create the `tasks.json` (inside `.vscode` folder)
        
    Use the following code

 ```json
 {
    "version": "2.0.0",
    "tasks": [
      {
        "identifier": "ng serve",
        "type": "npm",
        "script": "start",
        "problemMatcher": [],
        "group": {
          "kind": "build",
          "isDefault": true
        }
      },
      {
        "identifier": "ng test",
        "type": "npm",
        "script": "test",
        "problemMatcher": [],
        "group": {
          "kind": "test",
          "isDefault": true
        }
      }
    ]
  }
  ```

  4. Press <kbd>CTRL</kbd> + <kbd>SHIFT</kbd> + <kbd>B</kbd>
  5. Press <kbd>F5</kbd>