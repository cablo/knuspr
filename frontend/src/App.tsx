import React from 'react';
import {Card, Flex} from "antd";
import {OrderTable} from "./tables/OrderTable";
import {ProductTable} from "./tables/ProductTable";

export const FETCH_BASE_URL = process.env.REACT_APP_FETCH_BASE_URL ?? '';

export function App() {
    return <>
        <Flex vertical align="center" style={{height: '100vh', padding: '0 100px'}}>
            <Card title="Products" size="default" style={{width: '100%', margin: '0 100px'}} hoverable>
                <ProductTable/>
            </Card>
            <Card title="Orders" size="default" style={{width: '100%', margin: '50px  100px'}} hoverable>
                <OrderTable/>
            </Card>
        </Flex>
    </>;
}
