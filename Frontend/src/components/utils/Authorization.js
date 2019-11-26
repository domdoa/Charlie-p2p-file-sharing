import jwt_decode from "jwt-decode";
import {localhostHttp, LOGIN, REGISTER} from "./constants";

export const getAuthToken = () => localStorage.Authorization ? localStorage.Authorization : null;

export const logOut = () => localStorage.clear();

export const getEmail = () => {
    let token = getAuthToken();
    let email;
    if(token){
        const decoded = jwt_decode(token);
        email = decoded.sub;
    }
    return email;
};

export const isLoggedIn = () => {
    try {
        const token = getAuthToken();
        if(token){
            const decoded = jwt_decode(token);

            const currentTime = Date.now() / 1000;
            if (decoded.exp < currentTime) {
                // Logout user
                localStorage.clear();
                return false;
            } else {
                return true
            }

        }
        return false;
    } catch (error) {
        console.error(error);
    }

};

export const logUser = (data, resolve, reject) => {
    let url = localhostHttp + LOGIN; // URL
    fetch(url, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json; charset=utf-8"
        }
    })
        .then(res => res.clone().json())
        .then(response => {
            const {Authorization} = response;
            localStorage.setItem("Authorization", Authorization);
            resolve()
        })
        .catch(e => {reject()}); // error
};

export const registerUser = (data, resolve, reject) => {
    let url = localhostHttp + REGISTER; // URL
    if (data.group)
        url += "?groupName=" + data.group;
    else
        url += "?inviteString=" + data.inviteString;
    console.log(data)
    fetch(url, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json; charset=utf-8"
        }
    })
        .then(res => res.clone().json())
        .then(response => {
            if( response && (response.Error || response.status) )
                reject();
            else
                resolve(response);
        })
        .catch(e => {
            reject()
        }); // error
};

export default {getEmail}