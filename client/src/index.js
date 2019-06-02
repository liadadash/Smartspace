import React from "react";
import ReactDOM from "react-dom";
import { HashRouter, Route } from "react-router-dom";
import { toast } from 'react-toastify';

import MainPage from "./components/mainPage/MainPage";
import Login from "./components/login/Login.js";
import Register from "./components/register/Register.js";
import ManageProfile from "./components/ManageProfile.js";
import Settings from "./components/settings/Settings";

import 'react-toastify/dist/ReactToastify.css';

// Call it once in your app. At the root of your app is the best place --> important for the messages to work
// usage demo at --> https://fkhadra.github.io/react-toastify/
toast.configure()

class App extends React.Component {
	componentDidMount() {

	}

	render() {
		return (
			<HashRouter>
				<Route path="/" exact component={Login} />
				<Route path="/register/" exact component={Register} />
				<Route path="/dashboard/:smartspace/:email/settings/" exact component={Settings} />
				<Route path="/dashboard/:smartspace/:email/" exact component={MainPage} />
				<Route path="/dashboard/:smartspace/:email/profile/" exact component={ManageProfile} />
			</HashRouter>
		);
	}
}

ReactDOM.render(<App />, document.querySelector("#root"));
