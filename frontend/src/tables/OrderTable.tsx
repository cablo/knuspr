import React, {useEffect, useState} from 'react';
import {Table} from "antd";
import {ORDERS_API} from "../App";

export function OrderTable() {
    const [data, setData] = useState<any>([]);

    useEffect(() => {
        (async () => {
            try {
                setData(await ORDERS_API.listUnpaid());
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
            title: 'Paid',
            dataIndex: 'paid',
            key: 'paid'
        },
        {
            title: 'Created',
            dataIndex: 'created',
            key: 'created'
        }
    ];

    return <Table columns={columns} dataSource={data} rowKey="id" pagination={false} loading={!data}/>;
}
