#!/bin/bash

#  JDBC Properties
export PROJECT_FACTORY_JDBC_URL="jdbc:mysql://localhost:3306/project_factory"
export PROJECT_FACTORY_JDBC_USERNAME="root"
export PROJECT_FACTORY_JDBC_PASSWORD="root"
export PROJECT_FACTORY_JDBC_INITIAL_SIZE="10"
export PROJECT_FACTORY_JDBC_MAX_ACTIVE="200"

# URL Properties
export ADMIN_URL="http://localhost:4202"

#  Schema for Hibernate
export PROJECT_FACTORY_HIBERNATE_DEFAULT_SCHEMA="project_factory"

#  Application configuration Details
export MAIL_HOST="email-smtp.us-west-2.amazonaws.com"
export MAIL_FROM="no-reply@revature.com"
export MAIL_USERNAME="AKIAJIBATEMJXG5ZSSFQ"
export MAIL_PASSWORD="Avi9WBstpee68g3jRHLlPAepGuRkpMDvN+7GKD1cjDVu"
export MAIL_SMTP_STARTTLS="true"
export MAIL_SMTP_AUTH="true"
export MAIL_SMTP_QUIT_WAIT="false"
export MAIL_SMTP_SSL="false"
export MAIL_PORT="587"

export EMAIL_REGARDS_URL="https://www.revature.com"
#  Security
export JWT_SIGNING_KEY="@v8nt^uqNBa3Xqoq2bn4J^n747Y@VEiODsId^@SNA&H96hZ0o6"
export JWT_MINUTES_TO_LIVE="20160"

export TOKEN_ENCRYPTION_KEY="zEBer4grhUXdO5PUi4vcYzyCDQw9sZ1d"