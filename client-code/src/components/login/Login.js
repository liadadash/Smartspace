import React from "react";
import { Link, Redirect } from "react-router-dom";
import { toast } from 'react-toastify';

import * as api from '../../api/smartspace';
import './Login.css';

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isUserLoggedIn: false,
            email: '',
            smartspace: '2019B.nadav.peleg',
            user: '',
            serverIp: localStorage.getItem("server_ip"),
            serverPort: localStorage.getItem("server_port")
        };

        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handleSmartspaceChange = this.handleSmartspaceChange.bind(this);
        this.handleServerIpChange = this.handleServerIpChange.bind(this);
        this.handleServerPortChange = this.handleServerPortChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleEmailChange(event) {
        this.setState({ email: event.target.value });
    }

    handleSmartspaceChange(event) {
        this.setState({ smartspace: event.target.value });
    }

    handleServerIpChange(event) {
        this.setState({ serverIp: event.target.value });
    }

    handleServerPortChange(event) {
        this.setState({ serverPort: event.target.value });
    }

    handleSubmit(event) {
        event.preventDefault();

        localStorage.setItem("server_ip", this.state.serverIp);
        localStorage.setItem("server_port", this.state.serverPort);
        api.updateServerDetails();

        api.getUserByKey(this.state.smartspace, this.state.email)
            .then(res => {
                this.setState({ user: res.data, isUserLoggedIn: true });
                toast.info("Logged in successfully");
            }
            ).catch(api.errorHandler);
    }

    render() {

        if (!this.state.isUserLoggedIn) {
            return (
                <div className="container">
                    <div className="form-signin bg-light border rounded my-5">
                        <form onSubmit={this.handleSubmit}>
                            <h1 className="h3 mb-4 text-center">Sign In</h1>

                            <div className="row">
                                <div className="col-8 pr-0">
                                    <input type="text" className="form-control" placeholder="Server Ip" value={this.state.serverIp} onChange={this.handleServerIpChange} required />
                                </div>
                                <div className="col-4 pl-0">
                                    <input type="text" className="form-control" placeholder="Port" value={this.state.serverPort} onChange={this.handleServerPortChange} required />
                                </div>
                            </div>

                            <input type="text" className="form-control" placeholder="User Smartspace" value={this.state.smartspace} onChange={this.handleSmartspaceChange} required />
                            <input type="email" className="form-control mt-2" placeholder="User Email" value={this.state.email} onChange={this.handleEmailChange} required />
                            <button className="btn btn-lg btn-primary btn-block mt-3" type="submit" >Sign in</button>

                            <hr />
                            <p className="my-0">
                                <Link to="/register/"> <small className="form-text text-muted">Or click here to sign up</small> </Link>
                            </p>

                        </form>
                    </div></div>
            );
        }

        return <Redirect to={`/dashboard/${this.state.smartspace}/${this.state.email}/`} />;
    }
}

export default Login;
