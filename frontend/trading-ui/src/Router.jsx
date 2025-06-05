import { BrowserRouter, Route, Routes } from 'react-router-dom';
// Currently, we should only have the Dashboard component
import Dashboard from './page/Dashboard/Dashboard';
// Initialization of Router Component
export default function Router() {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<Dashboard />} />
                <Route exact path="/dashboard" element={<Dashboard />} />
            </Routes>
        </BrowserRouter>
    )
}