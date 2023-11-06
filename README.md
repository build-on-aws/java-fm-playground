# ☕ Java FM Playground

Welcome to the Java Foundation Model (FM) Playground! This is your go-to sample application for exploring how to utilize **Amazon Bedrock** using Java.

> 🚨 **Note:** This is a sample application and not intended for production use. It's all about learning and experimenting!

## What's Inside?

This repository offers a 🌱 **Spring Boot** application with a **Next.js** frontend that can be executed locally.

👇 Check out the screenshot below to see the app in action.

![Screenshot of the Java FM Playground, showing an example of a chat with Anthropic Claude v2](screenshot.png)

## Prerequisites

Before diving in, make sure you have the following installed:

- Java JDK 17 or higher (check out [Amazon Corretto](https://aws.amazon.com/corretto), a free distro of the OpenJDK)
- [Apache Maven](https://maven.apache.org/install.html)
- [Node.js (18.17 or later) and npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm) (for the Next.js frontend)
- An active [AWS account](https://aws.amazon.com/free/) with configured credentials

> 🚨 **Note:** AWS accounts don't have access to models by default. You can [add access to specific models](https://docs.aws.amazon.com/bedrock/latest/userguide/model-access.html#add-model-access) using the model access page.

## How to Deploy

Before proceeding, ensure you've met all the prerequisites above.

### 🐧 Linux

```bash
# Clone the repository
git clone https://github.com/build-on-aws/java-fm-playground.git

# Navigate into the directory
cd java-fm-playground

# Deploy the application
./deploy.sh
```

### 💻 Windows

```bash
# Clone the repository
git clone https://github.com/build-on-aws/java-fm-playground.git

# Navigate into the directory
cd java-fm-playground

# Deploy the application
deploy.cmd
```

## Access the Application

After successful deployment, open your web browser and navigate to: http://localhost:3000

You should now see the Java FM Playground up and running!

Have fun!

## License

This library is licensed under the MIT-0 License. See the [LICENSE](LICENSE) file.

## Security

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.
