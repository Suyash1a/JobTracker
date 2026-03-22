import React, { useEffect, useState } from 'react';
import { DragDropContext, Droppable, Draggable } from '@hello-pangea/dnd';
import axios from 'axios';
import JobCard from './JobCard';
import AddJobForm from './AddJobForm';
import StatsPanel from './StatsPanel';

const API_URL = 'http://52.54.113.243:8080/api/jobs';

const COLUMNS = [
    { id: 'Applied', label: '📨 Applied', color: '#3182ce' },
    { id: 'Shortlisted', label: '⭐ Shortlisted', color: '#805ad5' },
    { id: 'Interview', label: '🎯 Interview', color: '#d69e2e' },
    { id: 'Offer', label: '🎉 Offer', color: '#38a169' },
    { id: 'Rejected', label: '❌ Rejected', color: '#e53e3e' },
];

function KanbanBoard() {
    const [jobs, setJobs] = useState([]);
    const [showForm, setShowForm] = useState(false);

    const fetchJobs = async () => {
        try {
            const response = await axios.get(API_URL);
            setJobs(response.data);
        } catch (error) {
            console.error('Error fetching jobs:', error);
        }
    };

    useEffect(() => {
        fetchJobs();
    }, []);

    const onDragEnd = async (result) => {
        const { destination, source, draggableId } = result;
        if (!destination) return;
        if (destination.droppableId === source.droppableId) return;

        try {
            await axios.put(`${API_URL}/${draggableId}/status`, {
                status: destination.droppableId,
            });
            fetchJobs();
        } catch (error) {
            console.error('Error updating status:', error);
        }
    };

    const getJobsByStatus = (status) =>
        jobs.filter((job) => job.status?.toUpperCase() === status.toUpperCase());

    return (
        <div>
            <StatsPanel jobs={jobs} />

            <div style={{ textAlign: 'center', marginBottom: '24px' }}>
                <button
                    onClick={() => setShowForm(!showForm)}
                    style={{
                        background: showForm ? '#742a2a' : '#2b6cb0',
                        color: 'white',
                        border: 'none',
                        padding: '10px 24px',
                        borderRadius: '8px',
                        cursor: 'pointer',
                        fontSize: '14px',
                        fontWeight: 'bold',
                    }}
                >
                    {showForm ? '✕ Cancel' : '+ Add Job'}
                </button>
            </div>

            {showForm && (
                <AddJobForm
                    onJobAdded={() => {
                        fetchJobs();
                        setShowForm(false);
                    }}
                />
            )}

            <DragDropContext onDragEnd={onDragEnd}>
                <div style={{
                    display: 'flex',
                    gap: '16px',
                    padding: '0 24px 24px',
                    overflowX: 'auto',
                }}>
                    {COLUMNS.map((col) => (
                        <Droppable
                            droppableId={col.id}
                            key={col.id}
                            isCombineEnabled={false}
                            isDropDisabled={false}
                        >
                            {(provided, snapshot) => (
                                <div
                                    ref={provided.innerRef}
                                    {...provided.droppableProps}
                                    style={{
                                        background: snapshot.isDraggingOver ? '#2d3748' : '#1a202c',
                                        border: `1px solid #2d3748`,
                                        borderTop: `3px solid ${col.color}`,
                                        borderRadius: '10px',
                                        padding: '12px',
                                        minWidth: '220px',
                                        minHeight: '400px',
                                        flex: '1',
                                        transition: 'background 0.2s',
                                    }}
                                >
                                    <h3 style={{
                                        fontSize: '13px',
                                        fontWeight: 'bold',
                                        marginBottom: '12px',
                                        color: col.color,
                                        letterSpacing: '1px',
                                    }}>
                                        {col.label}
                                        <span style={{
                                            marginLeft: '8px',
                                            background: '#2d3748',
                                            color: '#a0aec0',
                                            borderRadius: '20px',
                                            padding: '1px 8px',
                                            fontSize: '11px',
                                        }}>
                      {getJobsByStatus(col.id).length}
                    </span>
                                    </h3>
                                    {getJobsByStatus(col.id).map((job, index) => (
                                        <Draggable
                                            key={job.id}
                                            draggableId={String(job.id)}
                                            index={index}
                                        >
                                            {(provided, snapshot) => (
                                                <div
                                                    ref={provided.innerRef}
                                                    {...provided.draggableProps}
                                                    {...provided.dragHandleProps}
                                                    style={{
                                                        ...provided.draggableProps.style,
                                                        opacity: snapshot.isDragging ? 0.8 : 1,
                                                    }}
                                                >
                                                    <JobCard job={job} />
                                                </div>
                                            )}
                                        </Draggable>
                                    ))}
                                    {provided.placeholder}
                                </div>
                            )}
                        </Droppable>
                    ))}
                </div>
            </DragDropContext>
        </div>
    );
}

export default KanbanBoard;