import React, {PureComponent} from "react";
import ChainElement from "./ChainElement";

class Chain extends PureComponent {

    render() {
        const chain = this.props.chain.map(c =>
            <div style={{display: "inline-block", margin: "1em"}} key={c.id}>
                <ChainElement data={c}/>
            </div>
        );
        return (
            <div>
                {chain}
            </div>
        )
    }
}

export default Chain