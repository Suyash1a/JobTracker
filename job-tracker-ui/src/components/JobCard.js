import React from 'react';

const tagColors = {
    TARGET: { bg: '#1c4532', color: '#68d391' },
    REACH: { bg: '#744210', color: '#f6e05e' },
    BACKUP: { bg: '#742a2a', color: '#fc8181' },
};

const sourceIcons = {
    LINKEDIN: '💼',
    NAUKRI: '🔍',
    REFERRAL: '🤝',
    COMPANY_WEBSITE: '🌐',
    OTHER: '📋',
    Linkedin: '💼',
    Naukri: '🔍',
    Referral: '🤝',
    Company_portal: '🌐',
};

function JobCard({ job }) {
    const tag = tagColors[job.companyTag?.toUpperCase()] || { bg: '#2d3748', color: '#a0aec0' };

    return (
        <div style={{
            background: '#1a202c',
            border: '1px solid #2d3748',
            borderRadius: '10px',
            padding: '14px',
            marginBottom: '10px',
            cursor: 'grab',
            transition: 'border-color 0.2s',
        }}
             onMouseEnter={e => e.currentTarget.style.borderColor = '#63b3ed'}
             onMouseLeave={e => e.currentTarget.style.borderColor = '#2d3748'}
        >
            <div style={{ fontWeight: 'bold', fontSize: '15px', marginBottom: '4px' }}>
                {job.company}
            </div>
            <div style={{ color: '#a0aec0', fontSize: '13px', marginBottom: '8px' }}>
                {job.role}
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <span style={{ fontSize: '12px', color: '#718096' }}>
          {sourceIcons[job.source] || '📋'} {job.source}
        </span>
                {job.companyTag && (
                    <span style={{
                        background: tag.bg,
                        color: tag.color,
                        padding: '2px 8px',
                        borderRadius: '20px',
                        fontSize: '11px',
                        fontWeight: 'bold',
                    }}>
            {job.companyTag}
          </span>
                )}
            </div>
            <div style={{ fontSize: '11px', color: '#4a5568', marginTop: '6px' }}>
                📅 {job.dateApplied}
            </div>
        </div>
    );
}

export default JobCard;