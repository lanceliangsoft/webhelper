import * as ReactDOM from 'react-dom';
import * as React from 'react';

import {App} from './app/App';

window.addEventListener("DOMContentLoaded", () => {
    ReactDOM.render(
        <App/>,
        document.getElementById("app")
    );
});
