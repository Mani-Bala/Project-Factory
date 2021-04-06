import { Constants } from './constants';

//  ------------ Batch Endpoint ------------

export const SERVER_END_POINT = Constants.getServerURL();
export const LOGIN_END_POINT = SERVER_END_POINT + 'login';
export const FORGOT_PASSWORD = SERVER_END_POINT + 'forgot-password';
export const RESET_PASSWORD = SERVER_END_POINT + 'secure/reset-password';
export const GET_PROJECTS = SERVER_END_POINT + 'secure/projects';
export const GET_PROJECT = SERVER_END_POINT + 'secure/project';
export const PROJECT_BY_ID = SERVER_END_POINT + 'secure/project/{projectId}';
export const GET_PROJECT_METADATA = SERVER_END_POINT + 'secure/metadata';

