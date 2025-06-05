import React, { useEffect, useState } from 'react'
import './Dashboard.scss'
import NavBar from '../../component/NavBar/NavBar'
import TraderList from '../../component/TraderList/TraderList'
import TraderListData from '../../component/TraderList/TraderListData.json'
import axios from 'axios'
import { Form,Input,Button, DatePicker, Modal } from 'antd'
import { createTraderUrl, deleteTraderUrl, tradersUrl } from '../../util/constants'
import "antd/dist/antd.min.css"

function Dashboard(props) {
    const [state,setState] = useState({
        isModalVisible: false,
        traders: []
    })
    const getTraders =() => {
        setState({
            ...state,
            traders: [...TraderListData]
        })
    }
    const showModal = () => {
        setState({
            ...state,
            isModalVisible: true
        })
    }
    const handleOk = async () => {
        await getTraders();
        setState({
            ...state,
            isModalVisible: false,
            firstName: null,
            lastName: null,
            dob: null,
            country: null,
            email: null,
        })
    }
    const onInputChange = (field,value) => {
        setState({
            ...state,
            [field]: value,
        })
    }
    const handleCancel = () => {
        setState({
            ...state,
            isModalVisible: false,
        })
    }
    useEffect(() => {
        getTraders()
    },[])

    const onTraderDelete = async(id) => {
        console.log("Trader " + id + " is deleted.")
        await axios.delete(deleteTraderUrl)
        await getTraders()
    }
  return (
        <div className="dashboard">
            <h2>Dashboard</h2>
            <NavBar />
            <div className="dashboard-content">
                <div className="title">
                    DashBoard
                    <div className="add-trader-button">
                        <Button onClick={showModal}>Add New Trader</Button>
                        <Modal title="Add new Trader" okText="Submit" visible={state.isModalVisible} onOk={handleOk} onCancel={handleCancel}>
                            <Form layout="vertical">
                                <div className="add-trader-form">
                                    <div className="add-trader-field">
                                        <Form.Item label="First Name">
                                            <Input allowClear={false} placeholder="John" onChange={(event) => onInputChange("firstname", event.target.value)} />
                                        </Form.Item>
                                    </div>
                                    <div className="add-trader-field">
                                        <Form.Item label="Last Name">
                                            <Input allowClear={false} placeholder="Doe" onChange={(event) => onInputChange("lastname", event.target.value)} />
                                        </Form.Item>
                                    </div>
                                    <div className="add-trader-field">
                                        <Form.Item label="Email">
                                            <Input allowClear={false} placeholder="johndoe@gmail.com" onChange={(event) => onInputChange("email", event.target.value)} />
                                        </Form.Item>
                                    </div>
                                    <div className="add-trader-field">
                                        <Form.Item label="Country">
                                            <Input allowClear={false} placeholder="Canada" onChange={(event) => onInputChange("country", event.target.value)} />
                                        </Form.Item>
                                    </div>
                                    <div className="add-trader-field">
                                        <Form.Item label="Date of Birth">
                                            <DatePicker style={{ width: "100%" }} placeholder="" onChange={(date, dateString) => onInputChange("dob", date.format("yyyy-MM-DD"))} />
                                        </Form.Item>
                                    </div>
                                </div>
                            </Form>
                        </Modal>
                    </div>
                </div>
                <TraderList onTraderDeleteClick={onTraderDelete} />
            </div>
        </div>
    )
}

export default Dashboard