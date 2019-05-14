import React, {PureComponent} from 'react'
import Chain from "./Chain";

export default class ChainList extends PureComponent {

    render() {
        console.log(this.props)
        const chainList = this.props.chainList.map((chain, index) =>
                <Chain key={index} chain={chain}/>
        );
        return (
            <div>
                {chainList}
            </div>
        )
    }
}