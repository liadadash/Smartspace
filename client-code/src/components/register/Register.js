import React from "react";
import { Redirect } from "react-router-dom";
import { toast } from 'react-toastify';

import * as api from '../../api/smartspace';
import './Register.css';

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            registerSuccess: false,
            email: '',
            username: '',
            avatar: '',
            role: 'PLAYER',
            user: null
        };
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handleAvatarChange = this.handleAvatarChange.bind(this);
        this.handleRoleChange = this.handleRoleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleEmailChange(event) {
        this.setState({ email: event.target.value });
    }

    handleUsernameChange(event) {
        this.setState({ username: event.target.value });
    }

    handleAvatarChange(event) {
        this.setState({ avatar: event.target.value });
    }

    handleRoleChange(event) {
        this.setState({ role: event.target.value });
    }

    handleSubmit(event) {
        event.preventDefault();
        api.registerAccount(this.state.email, this.state.role, this.state.username, this.state.avatar).then(res => {
            this.setState({ user: res.data, registerSuccess: true });
            toast.info("Success! You are now logged in.");
        }
        ).catch(api.errorHandler);

    }

    render() {
        if (!this.state.registerSuccess) {
            return (
                <div className="container">
                    <div className="form-signin bg-light border rounded mt-5">
                        <form onSubmit={this.handleSubmit}>
                            <h1 className="h3 mb-4 mt-2 text-center">Sign Up</h1>
                            <hr />
                            <input type="email" className="form-control mb-3" placeholder="User Email" value={this.state.email} onChange={this.handleEmailChange} required />
                            <input type="text" className="form-control mb-3" placeholder="Username" value={this.state.username} onChange={this.handleUsernameChange} required />
                            <input type="text" className="form-control mb-3" placeholder="Avatar (image url)" value={this.state.avatar} onChange={this.handleAvatarChange} required />

                            <select className="form-control" onChange={this.handleRoleChange}>
                                <option defaultValue>PLAYER</option>
                                <option>MANAGER</option>
                                <option>ADMIN</option>
                            </select>

                            <button className="btn btn-lg btn-primary btn-block mt-3" type="submit" >Sign up</button>
                        </form>
                    </div>
                </div>
            );
        }

        let userData = this.state.user;
        return <Redirect to={`/dashboard/${userData.key.smartspace}/${userData.key.email}/`} />;
    }
}

export default Login;
