# Java FM Playground

Java FM Playground is a sample application showcasing how to leverage Amazon Bedrock from Java code. 
As any sample application, it is not production-ready. It is provided for the sole purpose of illustrating 
how Java developers can leverage Amazon Bedrock to build generative AI-enabled applications.

Amazon Bedrock is a fully managed service that offers a choice of high-performing foundation models (FMs) from 
leading AI companies like AI21 Labs, Anthropic, Cohere, Meta, Stability AI, and Amazon via a single API.

In order to let you test and interact with the different foundation models, the Java FM Playground offers you
the following functionality:

- List all available Foundation Models
- Display details of individual Foundation Models

## Deploy the application

This guide will walk you through the steps to deploy the application on different operating systems.
Follow the instructions for your specific OS.

### Prerequisites
- Make sure you have the necessary permissions to execute scripts.
- Ensure that all environment variables and dependencies are properly set up.
- JDK installed (at least Java 11)
- Maven installed. 
- AWS CDK installed. 
- Docker installed and running.
- An active AWS account with configured credentials.

### Linux / MacOS
To deploy on Linux or MacOS, open your terminal and run the following command:

```
./install.sh
```

**Note:** You may be prompted to confirm to set IAM permissions for the application by typing 'Y'.

### Windows

Windows users have the option to use either PowerShell or the Command Prompt.

#### PowerShell

Open PowerShell and run:

```
.\Install.ps1
```

**Note:** You may be prompted to confirm to set IAM permissions for the application by typing 'Y'.

#### Command Prompt

Alternatively, you can use the Command Prompt. Open it and execute:

```
install.bat
```

**Note:** You may be prompted to confirm to set IAM permissions for the application by typing 'Y'.

## Start the Application
After running the appropriate script, you will see an output like:

```
Deployment succeeded. Navigate to https://PREFIX.cloudfront.net
```

Open your web browser and navigate to the displayed URL to start using the application.

## Delete the application

To delete the application, execute the following commands:

```     
cd ./cdk
cdk destroy --all
```

## Security

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.

## License

This library is licensed under the MIT-0 License. See the LICENSE file.