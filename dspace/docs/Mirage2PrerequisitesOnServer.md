# Mirage2 Prerequisites on Server
Follow the [instructions](../../dspace-xmlui-mirage2/readme.md#prerequisites-for-osx--linux) from the main dspace-xmlui-mirage2 project to install the prerequisites. The installation might not be starightforward on a redhat server.

###Possible issues and workarounds:
* All the installation commands from the above insturctions has to be run the service account user.
* The service account user needs to have sudo privilege to successfully install ruby.
* Ruby installtion guidelines:
  * Start the installation  
  `\curl -sSL https://get.rvm.io | bash -s stable --ruby`
  * If you get a prompt to import signature, import the signature and start the installation again.
  
    ```
    $ gpg2 --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3`
    $ \curl -sSL https://get.rvm.io | bash -s stable --ruby
    ```
  * Enter the (sudo-enabled) service account password when prompted with the following message:
  `<USER> password required for 'rhn-channel -l'`
  * Press `ctrl+c` when the following prompt appears: 
  
    ```
    Enabling optional repository
    Password: Username:
    ```
  * Intallation would proceed and complete.
* Continue to install the gems as per the instructions.

**Note:** If you get `command not found` messages after successfull installation, restart your shell session.