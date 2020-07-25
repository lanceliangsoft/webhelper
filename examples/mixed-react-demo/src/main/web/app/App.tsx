import * as React from 'react';

interface IAppProps {

}

interface IAppState {

}

export class App extends React.Component<IAppProps, IAppState> {
    readonly state : IAppState = {}

    render() {
        return <div>
                <h1>
                    Hello React World!
                </h1>
            </div>;
    }
}
