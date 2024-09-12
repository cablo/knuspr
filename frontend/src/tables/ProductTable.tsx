import React, {useEffect, useState} from 'react';
import {Table} from "antd";
import {PRODUCTS_API} from "../App";

export function ProductTable() {
    const [data, setData] = useState<any>([]);

    useEffect(() => {
        (async () => {
            try {
                setData(await PRODUCTS_API.listAll1());
            } catch (e) {
                console.error(e);
            }
        })();
    }, []);

    const columns = [
        {
            title: 'id',
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name'
        },
        {
            title: 'Quantity',
            dataIndex: 'quantity',
            key: 'quantity'
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price'
        },
        {
            title: 'Deleted',
            dataIndex: 'deleted',
            key: 'deleted'
        }
    ];

    return <Table columns={columns} dataSource={data} rowKey="id" pagination={false} loading={!data}/>;
}
