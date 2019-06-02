import axios from 'axios';
import { toast } from 'react-toastify';

var http = null;

if (!localStorage.getItem("server_ip")) {
    localStorage.setItem("server_ip", "localhost");
}
if (!localStorage.getItem("server_port")) {
    localStorage.setItem("server_port", "8080");
}

export const updateServerDetails = () => {
    const serverIp = localStorage.getItem("server_ip");
    const serverPort = localStorage.getItem("server_port");

    http = axios.create({
        baseURL: `http://${serverIp}:${serverPort}/smartspace/`,
        //headers: {
        //   dont remove this might use later
        //}
    });
}

updateServerDetails();

// user login
export const getUserByKey = (smartspace, email) => {
    return http.get(`users/login/${smartspace}/${email}`);
}

// register user
export const registerAccount = (email, role, username, avatar) => {
    const body = {
        "email": email,
        "role": role,
        "username": username,
        "avatar": avatar
    }

    return http.post('users', body);
}

// Update user 
// example usage: api.updateUser("2019B.nadav.peleg", "player@gmail.com", {avatar: "image.png"});
// example usage: api.updateUser("2019B.nadav.peleg", "player@gmail.com", {role: "MANAGER", avatar: "image.png"});
export const updateUser = (smartspace, email, { role, username, avatar }) => {
    let body = {}

    if (role != null) {
        body["role"] = role;
    }
    if (username != null) {
        body["username"] = username;
    }
    if (avatar != null) {
        body["avatar"] = avatar;
    }

    return http.put(`users/login/${smartspace}/${email}`, body);
}

// Update element 
export const updateElement = (managerSmartspace, managerEmail, elementSmartspace, elementId, { elementName, elementType, elementProperties }) => {
    let body = {}

    if (elementName != null) {
        body["name"] = elementName;
    }
    if (elementType != null) {
        body["elementType"] = elementType;
    }
    if (elementProperties != null) {
        body["elementProperties"] = elementProperties;
    }

    return http.put(`elements/${managerSmartspace}/${managerEmail}/${elementSmartspace}/${elementId}`, body);
}

// get element
export const getElementByKey = (smartspace, email, elementSmartspace, elementId) => {
    return http.get(`elements/${smartspace}/${email}/${elementSmartspace}/${elementId}`);
}

// get All elements using pagination
export const getAllElementsUsingPagination = (smartspace, email, page = 0, size = 10) => {
    return http.get(`elements/${smartspace}/${email}`, {
        params: { page: page, size: size }
    });
}

// search elements by type
export const getElementsByType = (smartspace, email, typeSearchStr, page = 0, size = 10) => {
    return http.get(`elements/${smartspace}/${email}`, {
        params: {
            search: 'type',
            value: typeSearchStr,
            page: page,
            size: size
        }
    });
}

// search elements by name
export const getElementsByName = (smartspace, email, nameSearchStr, page = 0, size = 10) => {
    return http.get(`elements/${smartspace}/${email}`, {
        params: {
            search: 'name',
            value: nameSearchStr,
            page: page,
            size: size
        }
    });
}

//get all elements by location
export const getAllElementsByLocatoin = (smartspace, email, x = 0, y = 0, distance = 10, page = 0, size = 10) => {
    return http.get(`elements/${smartspace}/${email}`, {
        params: {
            search: 'location',
            x: x,
            y: y,
            distance: distance,
            page: page,
            size: size
        }
    });
}

// invoke actions
export const invokeAction = (actionType, elementSmartspace, elementId, playerSmartspace, playerEmail, properties = {}) => {
    const body = {
        "type": actionType,
        "element": { "smartspace": elementSmartspace, "id": elementId },
        "player": { "smartspace": playerSmartspace, "email": playerEmail },
        "properties": properties
    }

    return http.post('actions', body);
}

// create new element
export const createNewElement = (managerSmartspace, managerEmail, elementType, name, lat, lng, properties = {}) => {
    const body = {
        "elementType": elementType,
        "name": name,
        "latlng": { "lat": lat, "lng": lng },
        "elementProperties": properties
    }

    return http.post(`elements/${managerSmartspace}/${managerEmail}`, body);
}

// # -------------------------- Bussiness Logic -------------------------- #

// create a new shopping list, will only work as manager.
export const createNewShoppingList = (smartspace, email, listName, listDesc, onlineIndicator) => {
    return createNewElement(smartspace, email, "ShoppingList", listName, 0, 0, {
        description: listDesc,
        onlineIndicator: onlineIndicator.key,
        members: []
    });
}

// create online indicator
export const createOnlineIndicator = (smartspace, email, listName) => {
    return createNewElement(smartspace, email, "OnlineIndicator", listName + "indicator", 0, 0, {
        onlineMembers: []
    });
}

// get all shopping lists for a user
export const getAllShoppingLists = (smartspace, email, page=0, size=100) => {
    return http.get(`shoppingLists/${smartspace}/${email}`, {
        params: {
            page: page,
            size: size
        }
    });
}

// get all shopping items for a list
export const getAllItemsByShoppingList = (userSmartspace, userEmail, listSmartspace, listId, page=0, size=100) => {
    return http.get(`shoppingLists/${userSmartspace}/${userEmail}/${listSmartspace}/${listId}`, {
        params: {
            page: page,
            size: size
        }
    });
}

// create a new shopping list, will only work as manager.
export const createNewShoppingItem = (userSmartspace, userEmail, listSmartspace, listId, itemName) => {
    return createNewElement(userSmartspace, userEmail, "ShoppingItem", itemName, 0, 0, {
        listKey: {smartspace: listSmartspace, id: listId},
        marked: false
    });
}

// invoke mark item action
export const invokeMarkItem = (elementSmartspace, elementId, playerSmartspace, playerEmail, markValue) => {
    return invokeAction("MarkItem", elementSmartspace, elementId, playerSmartspace, playerEmail, {
        markStatus: markValue
    });
}

// check in
export const invokeCheckIn = (elementSmartspace, elementId, playerSmartspace, playerEmail) => {
    return invokeAction("CheckIn", elementSmartspace, elementId, playerSmartspace, playerEmail);
}

// check out
export const invokeCheckOut = (elementSmartspace, elementId, playerSmartspace, playerEmail) => {
    return invokeAction("CheckOut", elementSmartspace, elementId, playerSmartspace, playerEmail);
}

// error handler for http requests
export const errorHandler = (err) => {
    if (err.response) {
        toast.error(err.response.data.message);
    }
    else {
        toast.error(err.toString());
    }
}