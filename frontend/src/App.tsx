import React from 'react';
import {Card, Flex} from "antd";
import {OrderTable} from "./tables/OrderTable";
import {ProductTable} from "./tables/ProductTable";

export const FETCH_BASE_URL = process.env.REACT_APP_FETCH_BASE_URL ?? '';

export function App() {
    return <>
        <Flex vertical align="center" style={{height: '100vh', padding: '0 100px'}}>
            <br/>
            <br/>
            <Card title="Products" size="default" style={{width: '100%'}} hoverable>
                <ProductTable/>
            </Card>
            <br/>
            <br/>
            <Card title="Orders" size="default" style={{width: '100%'}} hoverable>
                <OrderTable/>
            </Card>
        </Flex>
    </>;
}
