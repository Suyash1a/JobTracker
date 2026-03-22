import React, { useState } from 'react';
import axios from 'axios';

const API_URL = 'http://52.54.113.243:8080/api/jobs';

const inputStyle = {
    background: '#2d3748',
    border: '1px solid #4a5568',
    borderRadius: '6px',
    color: '#e2e8f0',
    padding: '8px 12px',
    width: '100%',
    marginBottom: '10px',
    fontSize: '14px',
};

function AddJobForm({ onJobAdded }) {
    const [form, setForm] = useState({
        company: '',
        role: '',
        dateApplied: '',
        source: 'LINKEDIN',
        notes: '',
        companyTag: 'TARGET',
    });

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async () => {
        try {
            await axios.post(API_URL, form);
            onJobAdded();
        } catch (error) {
            console.error('Error adding job:', error);
        }
    };

    return (
        <div style={{
            background: '#1a202c',
            border: '1px solid #2d3748',
            borderRadius: '12px',
            padding: '24px',
            maxWidth: '480px',
            margin: '0 auto 24px',
        }}>
            <h3 style={{ marginBottom: '16px', color: '#63b3ed' }}>New Application</h3>
            <input style={inputStyle} name="company" placeholder="Company" onChange={handleChange} />
            <input style={inputStyle} name="role" placeholder="Role" onChange={handleChange} />
            <input style={inputStyle} name="dateApplied" type="date" onChange={handleChange} />
            <select style={inputStyle} name="source" onChange={handleChange}>
                <option value="LINKEDIN">LinkedIn</option>
                <option value="NAUKRI">Naukri</option>
                <option value="REFERRAL">Referral</option>
                <option value="COMPANY_WEBSITE">Company Website</option>
                <option value="OTHER">Other</option>
            </select>
            <select style={inputStyle} name="companyTag" onChange={handleChange}>
                <option value="TARGET">🎯 Target</option>
                <option value="BACKUP">🛡️ Backup</option>
                <option value="REACH">🚀 Reach</option>
            </select>
            <input style={inputStyle} name="notes" placeholder="Notes (optional)" onChange={handleChange} />
            <button
                onClick={handleSubmit}
                style={{
                    background: '#2b6cb0',
                    color: 'white',
                    border: 'none',
                    padding: '10px 24px',
                    borderRadius: '8px',
                    cursor: 'pointer',
                    fontWeight: 'bold',
                    width: '100%',
                    fontSize: '14px',
                }}
            >
                Add Application
            </button>
        </div>
    );
}

export default AddJobForm;