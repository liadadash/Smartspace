import React from "react";
import Spinner from "./Spinner";
import { Link } from 'react-router-dom';

function ProfileDropdown(props) {
    let user = props.user;
    if (user != null) {
        return (
            <div>
                <div className="" data-toggle="dropdown">
                    <img className="rounded-circle p-3" src={user.avatar} alt={user.avatar} width="64px" height="64px" />
                    <small className="dropdown-toggle"></small>
                </div>
                <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <CreateRow title="View Profile" link="./profile" icon="info-sign" />
                    <CreateRow title="Socket Settings" link="./settings" icon="info-sign" />
                    <CreateRow title="Log Out" link="/" icon="log-out" />
                </div>
            </div>
        );
    }

    return <div className="p-3" style={{height: "64px"}}><Spinner /></div>
}

function SmartspaceLink(props) {
    let user = props.user;
    if (user != null) {
        return <Link className="navbar-brand mt-2 " to={`/dashboard/${user.key.smartspace}/${user.key.email}/`}>Smartspace</Link>
    }

    // don't use <Link> here because Router might not be mounted yet
    return <a className="navbar-brand mt-2 " href="/">Smartspace</a>
}

export function NavBar(props) {
    let user = props.user;
    return (
        <nav className="navbar navbar-light bg-light">
            <div className="w-100">
                <div className="float-left">
                    <div className="dropdown ml-2">
                        <ProfileDropdown user={user} />
                    </div>
                </div>
                <div className="float-right">
                    <SmartspaceLink user={user}/>
                </div>
            </div>
        </nav>
    );
}

function CreateRow(props) {
    return (
        <Link className="dropdown-item" to={props.link}>
            <div className="row">
                <div className="col">{props.title}</div>
                <div className="col-3 text-center"><span className={`glyphicon glyphicon-${props.icon}`}></span></div>
            </div>
        </Link>
    );
}

export default NavBar;
