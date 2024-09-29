/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'transparent': 'transparent',
        'gray': '#A3A3A3',
        'back-light': '#F3F3F3',
        'back-dark': '#4D4D4D',
        'custom1': '#A1D8F8',
        'custom2': '#F96E47',
        'custom3': '#F9C847',
        'custom4': '#AFF8C7',
        'text': '#000000',
      },
    },
  },
  plugins: [],
}

