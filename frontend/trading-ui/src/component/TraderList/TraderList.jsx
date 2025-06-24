import React, { useEffect, useState } from "react";
import {Table} from 'antd';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import 'antd/dist/antd.css';
import './TraderList.scss';
//import TraderListData from './TraderListData.json' ;
import {
    faTrashAlt as deleteIcon
} from '@fortawesome/free-solid-svg-icons';
import {
    faMoneyBillTransfer as traderListIcon
} from '@fortawesome/free-solid-svg-icons';
import { text } from "@fortawesome/fontawesome-svg-core";
import { useNavigate } from "react-router-dom";

function TraderList(props){
    const navigate = useNavigate();
    const columns=[
        {
            title: 'First Name',
            dataIndex: 'firstName',
            key: 'firstName',
            filters:[
            {
                text: 'Mike',
                value: 'Mike',
            },
            {
                text:'Jack',
                value:'Jack',
            },
            {
                text:'Robert',
                value:'Robert',
            },
            {
                text:'Piyush',
                value:'Piyush',
            }
            ],
            onFilter: (value, record) => record.firstName.toLowerCase().includes(value.toLowerCase()),
        },
        {
            title: 'Last Name',
            dataIndex: 'lastName',
            key: 'lastName',
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: 'Date of Birth',
            dataIndex: 'dob',
            key: 'dob',
            // sorter: (a, b) => new Date(a.dob).getTime() - new Date(b.dob).getTime()
            //sorter: (a, b) => new Date(a.dob || 0) - new Date(b.dob || 0)
            sorter: (a, b) => {
            const dateA = a.dob ? new Date(a.dob).getTime() : 0;
            const dateB = b.dob ? new Date(b.dob).getTime() : 0;
            return dateA - dateB;
            }


        },
        {
            title: 'Country',
            dataIndex: 'country',
            key: 'country',
        },
        {
            title: 'Actions',
            dataIndex: 'actions',
            key: 'actions',
            render: (text, record) => (
                    <div className="trader-action-icons">
                        <FontAwesomeIcon icon={ traderListIcon} className="view-icon" title="View Account" onClick={() => navigate(`/traders/${record.id}`)} />
                        <FontAwesomeIcon icon={ deleteIcon } className="delete-icon" title="Delete Trader" onClick={() => props.onTraderDeleteClick(record.id) } />
                    </div>
            ),
        },
    ];

    const [tableColumns, setTableColumns] = useState(columns)
    // const [dataSource, setDataSource] = useState([])
    

    // useEffect(() => {
    //     const dataSource = TraderListData
    //     setDataSource(dataSource)
    // },[])

  return (
   <Table
   dataSource={props.traders}
   columns={tableColumns}
   pagination={false}
   rowKey="id"
   />
  )
}

export default TraderList