import React from 'react';

function StatsPanel({ jobs }) {
    const total = jobs.length;
    const responded = jobs.filter(j => j.status?.toUpperCase() !== 'APPLIED').length;
    const offers = jobs.filter(j => j.status?.toUpperCase() === 'OFFER').length;
    const interviews = jobs.filter(j => j.status?.toUpperCase() === 'INTERVIEW').length;
    const responseRate = total > 0 ? ((responded / total) * 100).toFixed(1) : 0;
    const offerRate = total > 0 ? ((offers / total) * 100).toFixed(1) : 0;

    const stats = [
        { label: 'Total Applied', value: total },
        { label: 'Interviews', value: interviews },
        { label: 'Offers', value: offers },
        { label: 'Response Rate', value: `${responseRate}%` },
        { label: 'Offer Rate', value: `${offerRate}%` },
    ];

    return (
        <div style={{
            display: 'flex',
            gap: '16px',
            padding: '24px 32px',
            justifyContent: 'center',
            flexWrap: 'wrap',
        }}>
            {stats.map((stat) => (
                <div key={stat.label} style={{
                    background: '#1a202c',
                    border: '1px solid #2d3748',
                    borderRadius: '12px',
                    padding: '16px 28px',
                    textAlign: 'center',
                    minWidth: '130px',
                }}>
                    <div style={{ fontSize: '28px', fontWeight: 'bold', color: '#63b3ed' }}>
                        {stat.value}
                    </div>
                    <div style={{ fontSize: '12px', color: '#718096', marginTop: '4px' }}>
                        {stat.label}
                    </div>
                </div>
            ))}
        </div>
    );
}

export default StatsPanel;