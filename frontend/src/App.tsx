import React from 'react';
import {Button, Card, Flex} from "antd";
import {OrderTable} from "./tables/OrderTable";
import {ProductTable} from "./tables/ProductTable";
import {Configuration, OrdersApi, ProductsApi} from "./client-gen";

export const CONFIG = new Configuration({basePath: process.env.REACT_APP_FETCH_BASE_URL ?? ''});
export const PRODUCTS_API = new ProductsApi(CONFIG);
export const ORDERS_API = new OrdersApi(CONFIG);

export function App() {
    return <>
        <Flex vertical align="center" style={{height: '100vh', padding: '0 100px'}}>
            <br/>
            <Button type="primary" href="/swagger-ui">Go To Swagger</Button>
            <br/>
            <Card title="Products" size="default" style={{width: '100%', marginBottom: '30px'}} hoverable>
                <ProductTable/>
            </Card>
            <Card title="Orders" size="default" style={{width: '100%'}} hoverable>
                <OrderTable/>
            </Card>
            &nbsp;
        </Flex>
    </>;
}
