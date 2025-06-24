import React, { useEffect, useState } from 'react'
import './Dashboard.scss'
import NavBar from '../../component/NavBar/NavBar'
import TraderList from '../../component/TraderList/TraderList'
//import TraderListData from '../../component/TraderList/TraderListData.json'
import axios from 'axios'
import { Form,Input,Button, DatePicker, Modal } from 'antd'
import { createTraderUrl, deleteTraderUrl, tradersUrl } from '../../util/constants'
import "antd/dist/antd.min.css"

function Dashboard(props) {
    const [state,setState] = useState({
        isModalVisible: false,
        traders: []
    })
    const[form] = Form.useForm();
    const getTraders = async() => {
        const response = await axios.get(tradersUrl);
        if(response)
        setState(prev => ({
            ...prev,
            traders: response.data || []
        }))
    }
    const showModal = () => {
        setState(prev => ({
            ...prev,
            isModalVisible: true
        }))
    }
    const handleOk = async () => {
        const paramUrl = `/firstName/${state.firstName}/lastName/${state.lastName}/dob/${state.dob}/country/${state.country}/email/${state.email}`;
        const response = await axios.post(createTraderUrl + paramUrl, {});
        await getTraders();
        form.resetFields();
        setState(prev => ({
            ...prev,
            isModalVisible: false,
            firstName: null,
            lastName: null,
            dob: null,
            country: null,
            email: null,
        }))
    }
    const onInputChange = (field,value) => {
        setState(prev => ({
            ...prev,
            [field]: value,
        }))
    }
    const handleCancel = () => {
        setState(prev => ({
            ...prev,
            isModalVisible: false,
        }))
    }
    useEffect(() => {
        getTraders()
    },[])

    const onTraderDelete = async(id) => {
        const paramUrl = `${deleteTraderUrl}/${id}`;
        console.log("Trader " + id + " is deleted.")
        await axios.delete(paramUrl)
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
                        <Modal title="Add new Trader" 
                        okText="Submit" 
                        open={state.isModalVisible}
                        onCancel={handleCancel} 
                        onOk={handleOk} 
                        >
                            <Form 
                            form={form}
                            layout="vertical"
                            // onSubmit={handleOk}
                            >
                            
                                <div className="add-trader-form">
                                    <div className="add-trader-field">
                                        <Form.Item label="First Name"
                                        name="First Name"
                                        rules={[{
                                            required: true,
                                        }]}
                                        >
                                            <Input allowClear={false} placeholder="John" onChange={(event) => onInputChange("firstName", event.target.value)} />
                                        </Form.Item>
                                    </div>
                                    <div className="add-trader-field">
                                        <Form.Item label="Last Name"
                                        name="Last Name"
                                        rules={[{
                                            required: true,
                                        }]}>
                                            <Input allowClear={false} placeholder="Doe" onChange={(event) => onInputChange("lastName", event.target.value)} />
                                        </Form.Item>
                                    </div>
                                    <div className="add-trader-field">
                                        <Form.Item label="Email"
                                        name="Email"
                                        rules={[{
                                            required: true,
                                        }]}
                                        >
                                            <Input allowClear={false} placeholder="johndoe@gmail.com" onChange={(event) => onInputChange("email", event.target.value)} />
                                        </Form.Item>
                                    </div>
                                    <div className="add-trader-field">
                                        <Form.Item label="Country"
                                        name="Country"
                                        rules={[{
                                            required: true,
                                        }]}>
                                            <Input allowClear={false} placeholder="Canada" onChange={(event) => onInputChange("country", event.target.value)} />
                                        </Form.Item>
                                    </div>
                                    <div className="add-trader-field">
                                        <Form.Item label="Date of Birth"
                                        name="Date of Birth"
                                        rules={[{
                                            required: true,
                                        }]}>
                                            <DatePicker style={{ width: "100%" }} placeholder="" onChange={(date) => onInputChange("dob",  date ? date.format('YYYY-MM-DD'): "")} />
                                        </Form.Item>
                                    </div>
                                </div>
                            </Form>
                        </Modal>
                    </div>
                </div>
                <TraderList onTraderDeleteClick={onTraderDelete} traders={state.traders} />
            </div>
        </div>
    )
}

export default Dashboard