import React from "react";
import { Redirect } from "react-router-dom";

import * as api from '../../api/smartspace';
import { getSocketClient } from '../../api/socket';

import NavBar from "../NavBar";
import './Settings.css';
import { toast } from "react-toastify";

class Settings extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            serverIp: localStorage.getItem("socket_ip"),
            serverPort: localStorage.getItem("socket_port"),
            smartspace: "",
            email: "",
            user: null,
            saved: false
        };

        this.handleServerIpChange = this.handleServerIpChange.bind(this);
        this.handleServerPortChange = this.handleServerPortChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        if (this.props.match && this.props.match.params) {
            let params = this.props.match.params;
            this.setState({ smartspace: params.smartspace, email: params.email });

            api.getUserByKey(params.smartspace, params.email)
                .then(res => this.setState({ user: res.data }))
                .catch(api.errorHandler);
        }
    }

    handleServerIpChange(event) {
        this.setState({ serverIp: event.target.value });
    }

    handleServerPortChange(event) {
        this.setState({ serverPort: event.target.value });
    }

    handleSubmit(event) {
        event.preventDefault();

        localStorage.setItem("socket_ip", this.state.serverIp);
        localStorage.setItem("socket_port", this.state.serverPort);

        let socketTest = getSocketClient();

        socketTest.on("connection_success", data => {
            this.setState({ saved: true });
            toast.success("Connected! Please refresh the page.");
        });

        socketTest.emit("test_connection", this.state.user.key);

        setTimeout(() => {
            if (!this.state.saved) {
                toast.error("Connection failed");
            }
        }, 2000);
    }

    render() {
        if (!this.state.saved) {
            return (
                <div>
                    <NavBar user={this.state.user} />
                    <div className="container">
                        <div className="form-signin bg-light border rounded my-5">
                            <form onSubmit={this.handleSubmit}>
                                <h1 className="h3 mb-4 text-center">Socket Server Settings</h1>

                                <div className="row">
                                    <div className="col-8 pr-0">
                                        <input type="text" className="form-control" placeholder="Socket Ip" value={this.state.serverIp} onChange={this.handleServerIpChange} required />
                                    </div>
                                    <div className="col-4 pl-0">
                                        <input type="text" className="form-control" placeholder="Port" value={this.state.serverPort} onChange={this.handleServerPortChange} required />
                                    </div>
                                </div>

                                <button className="btn btn-lg btn-primary btn-block mt-0" type="submit" >Save</button>
                            </form>
                        </div>
                    </div>
                </div>
            );
        }

        return <Redirect to={`/dashboard/${this.state.smartspace}/${this.state.email}/`} />;
    }
}

export default Settings;
