import * as React from 'react';
import "./App.css";
import {ProcessStat, DemoService} from 'service/RestService';

type IAppProps = {

}

type IAppState = {
    processes : ProcessStat[];
}

export class App extends React.Component<IAppProps, IAppState> {
    readonly state : IAppState = {}

    async componentDidMount(): void {
        this.setState({processes: await DemoService.getProcesses()});
    }

    render() {
        return (
            <div>
                <h1>Processes</h1>
                <table>
                  <tr>
                    <th>pid</th><th>user</th><th>name</th><th>%cpu</th><th>%mem</th>
                  </tr>
                  { (this.state.processes || [])
                      .map(ps => <tr>
                          <td>{ps.pid}</td>
                          <td>{ps.user}</td>
                          <td>{ps.processName}</td>
                          <td>{ps.cpu}</td>
                          <td>{ps.mem}</td>
                      </tr>)
                  }
                </table>
            </div>
        );
    }
}
