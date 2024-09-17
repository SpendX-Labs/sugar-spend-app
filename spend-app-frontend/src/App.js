import "./App.css";
import MiniDrawer from "./main/MiniDrawer";
import Login from "./main/Login";
import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import { useCookies } from "react-cookie";
import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { actionCreators } from "./state/ActionCreator";
import { bindActionCreators } from "redux";
import axios from "axios";
import { COMMON_URL } from "./constants/URL";
import Loading from "./main/Loading";
import { ThemeProvider, createTheme } from "@mui/material";
import { ToastContainer } from "react-toastify";
import SignUp from "./main/SignUp";

function App() {
  const [cookies, setCookie] = useCookies(['access_token']);
  const dispatch = useDispatch();
  const actions = bindActionCreators(actionCreators, dispatch);
  const userData = useSelector(state => state.login);
  const [isLogging, setIsLogging] = React.useState(true);
  const [isLoading, setIsLoading] = React.useState(true);
  

  const uiTheme = createTheme({
    palette: {
      primary: {
        main: '#1e3a8a',
      },
      secondary: {
        main: '#60a5fa',
      },
      background: {
        default: '#e0f2fe',
      },
      text: {
        primary: '#0f172a',
      },
      mode: 'light',
    },
  });

  React.useEffect(() => {
    if (!cookies?.['access_token']) {
      setIsLoading(false);
      return;
    }
    axios.defaults.headers.common['Authorization'] = cookies['access_token'];
    axios.get(COMMON_URL + "auth/userinfo").then((response) => {
      if (response.status === 200 && response.data) {
        actions.loginAction(response.data);
        setIsLogging(false);
      }
    }).catch((error) => {
      console.error('userinfo failed:', error);
    }).finally(() => {
      setIsLoading(false);
    });
  }, []);

  return (
    <ThemeProvider theme={uiTheme}>
      <Router>
        <Routes>
          {isLogging && !userData?.username ?
            <Route path="*" element={isLoading ? <Loading /> : <Navigate to="/login" />} /> :
            (<><Route path="/" element={<Navigate to="/dashboard" />} />
              <Route path="/dashboard" element={<MiniDrawer />} /></>)}
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<SignUp />} />
        </Routes>
      </Router>
      <ToastContainer autoClose={3000} />
    </ThemeProvider>
  );
}

export default App;