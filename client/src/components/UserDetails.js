import React from "react";
import Spinner from "./Spinner";

function UserCard(props) {
    return (
        <div className="card mx-auto w-50">

            <div className="card-header">
                {props.title}
            </div>

            <div className="card-body text-left">
                {props.children}
            </div>
        </div>
    );
}

function TextField(props) {
    return (
        <div className="card-text form-group">
            <label htmlFor={props.id}>{props.label}</label>
            <input type="text" className="form-control" id={props.id} placeholder={props.label} value={props.value} disabled={props.disabled} onChange={props.onChange} />
        </div>
    );
}

class UserDetails extends React.Component {
    
    constructor(props) {
        super(props);
        this.state = { username: "", avatar: "" };

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handleAvatarChange = this.handleAvatarChange.bind(this);
    }

    componentDidUpdate(prevProps) {
        if (this.props.user && (this.props.user !== prevProps.user)) {
            let user = this.props.user;
            this.setState({ username: user.username, avatar: user.avatar });
        }
    }

    handleUsernameChange(event) {
        this.setState({ username: event.target.value });
    }

    handleAvatarChange(event) {
        this.setState({ avatar: event.target.value });
    }

    render() {
        if (this.props.user) {
            let user = this.props.user;

            return (
                <UserCard title={`${user.username}'s Profile`}>
                    <p className="card-text mb-2 lead text-center"> <img className="rounded" src={user.avatar} alt={user.avatar} height="80px" width="80px" /></p>
                    <p className="card-text mb-0 lead text-center">Role: {user.role}</p>
                    <p className="card-text mt-0 lead text-center">Points: {user.points}</p>

                    <hr />

                    <form onSubmit={this.props.handleProfileUpdate}>
                        <TextField id="smartspace" label="User Smartspace" value={user.key.smartspace} disabled />
                        <TextField id="email" label="User Email" value={user.key.email} disabled />
                        <TextField id="username" label="Username" value={this.state.username} onChange={this.handleUsernameChange} />
                        <TextField id="avatar" label="Avatar" value={this.state.avatar} onChange={this.handleAvatarChange} />
                        <button className="btn btn-lg btn-primary btn-block mt-3" type="submit" >Update</button>
                    </form>
                </UserCard>
            );
        }

        return <UserCard title="My Profile"><Spinner /></UserCard>;
    }
}

export default UserDetails;