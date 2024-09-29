import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import './index.css'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <div className="max-h-screen h-screen min-w-screen w-screen">
      <App />
    </div>
  </StrictMode>,
)