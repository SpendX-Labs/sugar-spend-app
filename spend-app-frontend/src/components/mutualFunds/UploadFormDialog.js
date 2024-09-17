import React, { useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    TextField,
} from '@mui/material';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { COMMON_URL } from '../../constants/URL';

const UploadFormDialog = ({ open, onClose }) => {
    const [cookies, setCookie] = useCookies(['access_token']);
    const [selectedFile, setSelectedFile] = useState(null);
    const [password, setPassword] = useState('');

    const handleFileChange = (event) => {
        const file = event.target.files[0];

        if (file) {
            const fileName = file.name.toLowerCase();

            if (fileName.endsWith('.pdf')) {
                setSelectedFile(file);
            } else {
                event.target.value = null;
                toast.error('Please select a valid .pdf file.', {
                    position: toast.POSITION.TOP_RIGHT,
                    style: { backgroundColor: 'red', color: '#fff' },
                });
            }
        }
    };

    const handleCancel = () => {
        onClose();
    };

    const handleSubmit = () => {
        if (password && selectedFile) {
            const formData = new FormData();
            formData.append('file', selectedFile);
            formData.append('password', password);
            axios.defaults.headers.common['Authorization'] = cookies['access_token'];
            axios.post(COMMON_URL + 'app/bulk-upload', formData)
                .then((response) => {
                    if (response.status === 200) {
                        toast.success('sucessfully file uploaded.', {
                            position: toast.POSITION.TOP_RIGHT,
                            style: { backgroundColor: 'green', color: '#fff' },
                        });
                    }
                    else {
                        toast.error('internal error while uploading file', {
                            position: toast.POSITION.TOP_RIGHT,
                            style: { backgroundColor: 'red', color: '#fff' },
                        });

                    }
                })
                .catch((error) => {
                    console.error('Error uploading file:', error);
                    toast.error('internal error while uploading file', {
                        position: toast.POSITION.TOP_RIGHT,
                        style: { backgroundColor: 'red', color: '#fff' },
                    });
                }).finally(() => {
                    onClose();
                });
        }
    };

    const isAnyFieldEmpty = () => {
        return !password || !selectedFile;
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth={'sm'}>
            <DialogTitle>Upload Cams and KFinTech File</DialogTitle>
            <DialogContent>
                
                <TextField
                    fullWidth
                    type="file"
                    label="Upload File"
                    onChange={handleFileChange}
                    style={{ marginTop: "16px" }}
                />
                <TextField
                    fullWidth
                    type="password"
                    label="Password"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    style={{ marginTop: "16px" }}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleCancel} color="primary">
                    Cancel
                </Button>
                <Button onClick={handleSubmit} color="primary" disabled={isAnyFieldEmpty()}>
                    Submit
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default UploadFormDialog;
